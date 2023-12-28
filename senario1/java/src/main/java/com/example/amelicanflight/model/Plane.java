package com.example.amelicanflight.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * DatabaseのPlaneテーブル用のEntity
 */

@Entity
@Getter
@Setter
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer planeId;

    String planeType;

    Integer totalSeats;

    public Plane() {
    }

    public Plane(Integer aId) {
        planeId = aId;
    }

    public Plane(String aPlaneType, Integer aTotalSeats) {
        planeType = aPlaneType;
        totalSeats = aTotalSeats;
    }
}
