package com.example.americanflight.type;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import com.example.americanflight.model.Airport;
import com.example.americanflight.util.BigDecimalSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.Setter;

/**
 * DtoAirport
 * 内部で使用するためのAirport用のDTOクラス
 * 
 */

@Getter
@Setter
public class DtoAirport extends Convertable {
    String airportCode;
    String airportName;
    String municipality;

    public DtoAirport() {
    }

    public DtoAirport(Airport aAirport) {
        airportCode = aAirport.getIataCode();
        airportName = aAirport.getName();
        municipality = aAirport.getMunicipality();

    }
}
