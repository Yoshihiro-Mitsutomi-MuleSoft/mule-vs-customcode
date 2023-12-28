package com.example.amelicanflight.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.amelicanflight.model.Flight;

/**
 * FlightRepository
 * フライトに関するデータベースとのインターフェース
 * findBy...とJPAのルールに従って記述することにより自動的にメソッドが生成される
 * @Cacheableについては、各キャッシュ保存用のスペースをCachingConfigクラスにて記述している
 */

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {
    @Cacheable("flights")
    public List<Flight> findAll();

    @Cacheable("flights")
    public List<Flight> findByDestinationAirport(String aDestination);

    @Cacheable("flight")
    public Optional<Flight> findById(Integer flightId);
}
