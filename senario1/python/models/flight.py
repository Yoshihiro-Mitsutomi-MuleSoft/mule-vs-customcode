from db import db


class FlightModel(db.Model):
    __tablename__ = "Flight"

    flightId = db.Column(db.Integer, primary_key=True)
    flightCode = db.Column(db.String(80), nullable=False)
    price = db.Column(db.Integer, nullable=False)
    departureDate = db.Column(db.Date, nullable=False)
    originAirport = db.Column(db.String(3), nullable=False)
    destinationAirport = db.Column(db.String(3), nullable=False)
    bookedSeats = db.Column(db.Integer, nullable=False)
    planeId = db.Column(
        db.Integer, db.ForeignKey("Plane.planeId"), unique=False, nullable=False
    )


    planes = db.relationship(
        "PlaneModel", back_populates="flights"
    )
