package com.example.americanflight.model;

import java.util.Date;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

/**
 * DatabaseのAirportテーブル用のEntity
 */

@Entity
@Getter
@Setter

public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String ident;
    String type;
    String name;
    @Column(name = "elevation_ft")
    Integer elevationFt;
    String continent;
    @Column(name = "iso_country")
    String isoCountry;
    @Column(name = "iso_region")
    String isoRegion;
    String municipality;
    @Column(name = "iata_code")
    String iataCode;
    String coordinates;
}
