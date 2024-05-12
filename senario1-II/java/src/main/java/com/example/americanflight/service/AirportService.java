package com.example.americanflight.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.StaleStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.americanflight.model.Airport;

import com.example.americanflight.repository.AirportRepository;
import com.example.americanflight.type.DtoAirport;
import com.example.americanflight.type.DtoAirportDetail;

/**
 * AirportService
 * 空港に関連するロジックを記述
 */
@Service
public class AirportService {

    @Autowired
    AirportRepository airportRepository;

    /**
     * getAirports
     * 
     * @param search
     * @return
     */
    @Retryable(retryFor = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public List<DtoAirport> getAirports(String aSearchName) {
        List<Airport> result;

        if (aSearchName == null) {
            result = airportRepository.findAll();
        } else {
	    result = airportRepository.findByNameLike("%" + aSearchName + "%");
        }

	return result.stream().map(DtoAirport::new).collect(Collectors.toList());
    }

    /**
     * getAirports By airportCode
     * 
     * @param search
     * @return
     */
    @Retryable(retryFor = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public Optional<DtoAirportDetail> getAirportDetail(String anairportCode) {

	Optional<Airport> result = airportRepository.findByIataCode(anairportCode);

        if (result.isPresent()) {
            return Optional.of(new DtoAirportDetail(result.get()));
        } else {
            return Optional.ofNullable(null);
        }

    }

    public boolean isAirportExists(String anairportCode) {
	return airportRepository.findByIataCode(anairportCode).isPresent();
    }

}
