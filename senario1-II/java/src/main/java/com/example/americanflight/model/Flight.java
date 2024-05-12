package com.example.americanflight.model;

import java.util.Date;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * DatabaseのFlightテーブル用のEntity
 */

@Entity
@Getter
@Setter

public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer flightId;
    String flightCode;
    Double price;
    Date departureDate;
    String originAirport;
    String destinationAirport;
    Integer bookedSeats;

    @OneToOne
    @JoinColumn(name="planeId", referencedColumnName="planeId")
    Plane plane;

    
}
