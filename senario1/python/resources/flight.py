from flask.views import MethodView
from flask_smorest import Blueprint, abort
from sqlalchemy import func
from sqlalchemy.exc import SQLAlchemyError, IntegrityError
from marshmallow import Schema

from db import db
from models import FlightModel, PlaneModel
from schemas import FlightSchema, FlightSearchQueryArgs, FlightReqResSchema, PlaneReqResSchema, PlainPlaneSchema
from deepdiff import DeepDiff
import json
from dogpile.cache import make_region
from time import sleep

blp = Blueprint("Flights", "Flights", description="Operations on Flight Information")

def formatFlight(flight: FlightModel):
    resFlight = FlightReqResSchema()
    resFlight.ID = flight.flightId
    resFlight.code = flight.flightCode
    resFlight.price = flight.price
    resFlight.departureDate = flight.departureDate.strftime('%Y/%m/%d')
    resFlight.origin = flight.originAirport
    resFlight.destination = flight.destinationAirport
    resFlight.emptySeats = flight.planes.totalSeats - flight.bookedSeats
    
    planeRes = PlaneReqResSchema()
    planeRes.type =  flight.planes.planeType
    planeRes.totalSeats = flight.planes.totalSeats
    resFlight.plane = planeRes

    return resFlight

def unPackFlight(flight_data: FlightReqResSchema):
    flightDb = FlightModel()
    flightDb.flightId = flight_data.get("ID", None)
    flightDb.flightCode = flight_data.get("code")
    flightDb.price = flight_data.get("price")
    flightDb.departureDate = flight_data.get("departureDate")
    flightDb.originAirport = flight_data.get("origin")
    flightDb.destinationAirport = flight_data.get("destination")

    return flightDb

def unPackPlane(flight_data: FlightReqResSchema):
    planeDb = PlaneModel()
    plane_data = flight_data.get("plane")
    planeDb.planeType = plane_data.get("type")
    planeDb.totalSeats = plane_data.get("totalSeats")

    return planeDb


# Dogpileキャッシュリージョンを作成
cache_region = make_region().configure(
    "dogpile.cache.memory",  # メモリキャッシュを使用
    expiration_time=3600,  # キャッシュの有効期限を設定（秒単位）
)

__max_retry_count__ = 3
sleep_for = 1

@blp.route("/flights/<string:flight_id>")
class Flight(MethodView):
    @blp.response(200, FlightReqResSchema)
    def get(self, flight_id):
        attempts = 0
        while True:
            attempts += 1
            try:
                flight = FlightModel.query.get_or_404(flight_id)
                resFlight = formatFlight(flight)
                return resFlight

            except SQLAlchemyError as e:
                if attempts <= self.__max_retry_count__:
                    print(
                            "/!\ Database operation error: retrying Strategy => sleeping for {}s"
                        " and will retry (attempt #{} of {}) \n Detailed query impacted: {}".format(
                            sleep_for, attempts, __max_retry_count__, e)
                    )
                    sleep(sleep_for)
                    continue
                else:
                    abort(500, message="An error occurred during data manipulation.")

    def delete(self, flight_id):
        attempts = 0
        while True:
            attempts += 1
            try:
                flight = FlightModel.query.get_or_404(flight_id)
                db.session.delete(flight)
                db.session.commit()
                return {"message": "Flight Record has been deleted"}, 200

            except SQLAlchemyError as e:
                if attempts <= self.__max_retry_count__:
                    print(
                            "/!\ Database operation error: retrying Strategy => sleeping for {}s"
                        " and will retry (attempt #{} of {}) \n Detailed query impacted: {}".format(
                            sleep_for, attempts, __max_retry_count__, e)
                    )
                    sleep(sleep_for)
                    continue
                else:
                    abort(500, message="An error occurred during data manipulation.")


    @blp.arguments(FlightReqResSchema)
    @blp.response(200)
    def put(self, flight_data, flight_id):
        trgFlight = FlightModel.query.get_or_404(flight_id)
        origFlightDictJson = {
            "code": trgFlight.flightCode,
            "departureDate": trgFlight.departureDate.strftime('%Y/%m/%d'),
            "destination": trgFlight.destinationAirport,
            "origin": trgFlight.originAirport,
            "emptySeats": trgFlight.planes.totalSeats - trgFlight.bookedSeats,
            "price": int(trgFlight.price),
            "plane": {
                "totalSeats": trgFlight.planes.totalSeats,
                "type": trgFlight.planes.planeType
            }
        }
        differences = DeepDiff(origFlightDictJson, flight_data)

        # handle planeInfo
        planeDb = unPackPlane(flight_data)
        plane_data = flight_data.get("plane")
        planes = planeDb.query.filter(PlaneModel.planeType==plane_data.get("type"), PlaneModel.totalSeats==plane_data.get("totalSeats")).all()


        attempts = 0
        while True:
            attempts += 1
            try:
                if len(planes) == 0:
                    db.session.add(planeDb)
                    newPlanes = planeDb.query.filter(PlaneModel.planeType==plane_data.get("type"), PlaneModel.totalSeats==plane_data.get("totalSeats"))
                    newPlane = newPlanes[0]
                    planeId = newPlane.planeId
                else:
                    origPlane = planes[0]
                    planeId = origPlane.planeId
                
                trgFlight.flightCode = flight_data.get("code")
                trgFlight.price = flight_data.get("price")
                trgFlight.departureDate = flight_data.get("departureDate")
                trgFlight.originAirport = flight_data.get("origin")
                trgFlight.destinationAirport = flight_data.get("destination")
                trgFlight.bookedSeats = plane_data.get("totalSeats") - flight_data.get("emptySeats")
                trgFlight.planeId = planeId

                db.session.commit()
                return json.loads(differences.to_json()), 200

            except IntegrityError:
                abort(
                    400,
                    message="A store with that name already exists.",
                )

            except SQLAlchemyError as e:
                if attempts <= self.__max_retry_count__:
                    db.session.rollback()
                    print(
                            "/!\ Database operation error: retrying Strategy => sleeping for {}s"
                        " and will retry (attempt #{} of {}) \n Detailed query impacted: {}".format(
                            sleep_for, attempts, __max_retry_count__, e)
                    )
                    sleep(sleep_for)
                    continue
                else:
                    abort(500, message="An error occurred during data manipulation.")


@blp.route("/flights")
class FlightList(MethodView):
    @blp.arguments(FlightSearchQueryArgs, location="query")
    @blp.response(200, FlightReqResSchema(many=True))
    @cache_region.cache_on_arguments()
    def get(self, query_dest, *args):
        qrParamDest = query_dest.get("destination", "")

        attempts = 0
        while True:
            attempts += 1
            try:
                flights = FlightModel.query.filter(FlightModel.destinationAirport.startswith(qrParamDest)).all()

                flightResList = []
                for flight in flights:
                    flightResList.append(formatFlight(flight))

                return flightResList

            except SQLAlchemyError as e:
                if attempts <= self.__max_retry_count__:
                    print(
                            "/!\ Database operation error: retrying Strategy => sleeping for {}s"
                        " and will retry (attempt #{} of {}) \n Detailed query impacted: {}".format(
                            sleep_for, attempts, __max_retry_count__, e)
                    )
                    sleep(sleep_for)
                    continue
                else:
                    abort(500, message="An error occurred during data manipulation.")

    @blp.arguments(FlightReqResSchema)
    def post(self, flight_data):
        flightDb = unPackFlight(flight_data)
        planeDb = unPackPlane(flight_data)
        planeId = 0
        newFlightId = 0

        plane_data = flight_data.get("plane")
        planes = planeDb.query.filter(PlaneModel.planeType==plane_data.get("type"), PlaneModel.totalSeats==plane_data.get("totalSeats")).all()

        attempts = 0
        while True:
            attempts += 1
            try:
                if len(planes) == 0:
                    db.session.add(planeDb)
                    newPlanes = planeDb.query.filter(PlaneModel.planeType==plane_data.get("type"), PlaneModel.totalSeats==plane_data.get("totalSeats"))
                    newPlane = newPlanes[0]
                    planeId = newPlane.planeId
                else:
                    origPlane = planes[0]
                    planeId = origPlane.planeId
                
                flightDb.bookedSeats = plane_data.get("totalSeats") - flight_data.get("emptySeats")
                flightDb.planeId = planeId
                db.session.add(flightDb)
                db.session.commit()

                newFlights = db.session.query(func.max(FlightModel.flightId))
                newFlight = newFlights[0]
                print(newFlight)
                newFlightId = list(newFlight)[0]

                return {"message": f"Insertion Completed. New ID is: {newFlightId}"}, 201

            except IntegrityError:
                abort(
                    400,
                    message="A store with that name already exists.",
                )

            except SQLAlchemyError as e:
                if attempts <= self.__max_retry_count__:
                    db.session.rollback()
                    print(
                            "/!\ Database operation error: retrying Strategy => sleeping for {}s"
                        " and will retry (attempt #{} of {}) \n Detailed query impacted: {}".format(
                            sleep_for, attempts, __max_retry_count__, e)
                    )
                    sleep(sleep_for)
                    continue
                else:
                    abort(500, message="An error occurred during data manipulation.")
        
