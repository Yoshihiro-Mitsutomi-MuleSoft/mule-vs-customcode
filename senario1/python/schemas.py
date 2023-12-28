from marshmallow import Schema, fields, validate


class PlainFlightSchema(Schema):
    flightId = fields.Int(dump_only=True)
    flightCode = fields.Str(required=True)
    price = fields.Int(required=True)
    departureDate = fields.Date(required=True)
    originAirport = fields.Str(required=True)
    destinationAirport = fields.Str(required=True)
    bookedSeats = fields.Int(required=True)


class PlainPlaneSchema(Schema):
    planeId = fields.Int(dump_only=True)
    planeType = fields.Str()
    totalSeats = fields.Int()


class FlightSchema(PlainFlightSchema):
    planes = fields.Nested(PlainPlaneSchema())


class FlightSearchQueryArgs(Schema):
    destination = fields.Str(
        validate=[
            validate.OneOf(["SFO", "LAX", "CLE"]),
        ]
    )


class PlaneReqResSchema(Schema):
    type = fields.Str(required=True)
    totalSeats = fields.Int(required=True)

class FlightReqResSchema(Schema):
    ID = fields.Int(dump_only=True)
    code = fields.Str(required=True)
    price = fields.Int(required=True)
    departureDate = fields.Str(required=True,
                               validate=validate.Regexp(
                                   '^\\d{4}\\/(0[1-9]|1[012])\\/(0[1-9]|[12][0-9]|3[01])$'
                               ))
    origin = fields.Str(required=True)
    destination = fields.Str(required=True)
    emptySeats = fields.Int(required=True)
    plane = fields.Nested(PlaneReqResSchema())
