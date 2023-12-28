from db import db


class PlaneModel(db.Model):
    __tablename__ = "Plane"

    planeId = db.Column(db.Integer, primary_key=True)
    planeType = db.Column(db.String(80), unique=False, nullable=False)
    totalSeats = db.Column(db.Integer, unique=False, nullable=False)

    flights = db.relationship("FlightModel", back_populates="planes")
