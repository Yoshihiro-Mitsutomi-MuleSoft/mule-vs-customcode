package com.example.amelicanflight.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.StaleStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.amelicanflight.model.Flight;
import com.example.amelicanflight.model.Plane;
import com.example.amelicanflight.repository.FlightRepository;
import com.example.amelicanflight.repository.PlaneRepository;
import com.example.amelicanflight.type.DtoFlight;
import com.example.amelicanflight.type.DtoPlane;

/**
 * FlightService
 * フライトに関連するロジックを記述
 */
@Service
public class FlightService {

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    PlaneRepository planeRepository;

    /**
     * getFlights
     * 指定のdestinationをもつフライトを取得する
     * @param aDestination
     * @return
     */
    @Retryable(retryFor = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public List<DtoFlight> getFlights(String aDestination) {
        List<Flight> result;

        if (aDestination != null) {
            result = flightRepository.findByDestinationAirport(aDestination);
        } else {
            result = flightRepository.findAll();
        }

        return result.stream().map(DtoFlight::new).collect(Collectors.toList());

    }

    /**
     * getFlightsById
     * 指定のIDによるフライトの取得
     * @param aId
     * @return
     */
    @Retryable(retryFor = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public Optional<DtoFlight> getFlightById(String aId) {
        Optional<Flight> result = flightRepository.findById(Integer.parseInt(aId));
        if (result.isPresent()) {
            return Optional.of(new DtoFlight(result.get()));
        } else {
            return Optional.ofNullable(null);
        }

    }

    /**
     * saveDtoFlight
     * 指定のフライトをDatabaseへ保存する。新規および更新はJPAにて処理される
     * @param aDtoFlight
     * @return
     */
    @Retryable(StaleStateException.class)
    @Transactional(rollbackFor = Exception.class)
    public DtoFlight saveDtoFlight(DtoFlight aDtoFlight) {
        DtoPlane dtoPlane = aDtoFlight.getPlane();
        Flight flight = aDtoFlight.toFlight();

        List<Plane> planes = planeRepository.findByPlaneTypeAndTotalSeats(dtoPlane.getPlaneType(),
                dtoPlane.getTotalSeats());
        if (planes.isEmpty()) {
            Plane existPlane = planes.get(0);
            flight.setPlane(existPlane);
        } else {
            Plane newPlane = new Plane(dtoPlane.getPlaneType(), dtoPlane.getTotalSeats());
            Plane savedPlane = planeRepository.save(newPlane);
            flight.setPlane(savedPlane);
        }
        Flight savedFlight = flightRepository.save(flight);
        var a = 1/0;
        return new DtoFlight(savedFlight);
    }

    /**
     * deleteDtoFlight
     * 指定のフライトの削除
     * @param aFlightId
     */
    @Retryable(StaleStateException.class)
    public void deleteDtoFlight(Integer aFlightId) {
        flightRepository.deleteById(aFlightId);
    }
}
