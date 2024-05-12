package com.example.americanflight.type;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import com.example.americanflight.model.Flight;
import com.example.americanflight.util.BigDecimalSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.Setter;

/**
 * DtoFlight
 * 内部で使用するためのFlight用のDTOクラス
 * 
 */

@Getter
@Setter
public class DtoFlight extends Convertable {
    Integer flightId;
    String flightCode;
    @JsonSerialize(using = BigDecimalSerializer.class)
    BigDecimal price;
    @JsonFormat(pattern = "yyyy/MM/dd")
    Date departureDate;
    String originAirport;
    String destinationAirport;
    Integer emptySeats;
    DtoPlane plane;

    public DtoFlight() {
    }

    public DtoFlight(Flight flight) {
        flightId = flight.getFlightId();
        flightCode = flight.getFlightCode();
        price = BigDecimal.valueOf(flight.getPrice());
        departureDate = flight.getDepartureDate();
        originAirport = flight.getOriginAirport();
        destinationAirport = flight.getDestinationAirport();
        emptySeats = flight.getPlane().getTotalSeats() - flight.getBookedSeats();
        plane = new DtoPlane();
        plane.planeType = flight.getPlane().getPlaneType();
        plane.totalSeats = flight.getPlane().getTotalSeats();
    }

    public static DtoFlight of(FlightsPostRequest flightPostRequest) {
        try {
            DtoFlight dtoFlight = new DtoFlight();
            dtoFlight.setFlightId(flightPostRequest.getID());
            dtoFlight.setFlightCode(flightPostRequest.getCode());
            dtoFlight.setPrice(flightPostRequest.getPrice());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            dtoFlight.setDepartureDate(sdf.parse(flightPostRequest.getDepartureDate()));
            dtoFlight.setOriginAirport(flightPostRequest.getOrigin());
            dtoFlight.setDestinationAirport(flightPostRequest.getDestination());
            dtoFlight.setEmptySeats(flightPostRequest.getEmptySeats());
            DtoPlane plane = new DtoPlane();
            plane.planeType = (String) flightPostRequest.getPlane().get("type");
            plane.totalSeats = (Integer) flightPostRequest.getPlane().get("totalSeats");
            dtoFlight.setPlane(plane);
            return dtoFlight;
        } catch (ParseException ex) {
            throw new ConstraintViolationException(ex.getMessage(), new HashSet<>());
        }
    }

    public Flight toFlight() {
        Flight flight = new Flight();
        flight.setFlightId(flightId);
        flight.setFlightCode(flightCode);
        flight.setPrice(price.doubleValue());
        flight.setDepartureDate(departureDate);
        flight.setOriginAirport(originAirport);
        flight.setDestinationAirport(destinationAirport);
        flight.setBookedSeats(plane.totalSeats - emptySeats);
        flight.setPlane(plane.toPlane());
        return flight;

    }
}
