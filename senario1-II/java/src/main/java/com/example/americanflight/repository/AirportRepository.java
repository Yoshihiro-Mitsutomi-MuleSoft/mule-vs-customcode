package com.example.americanflight.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.americanflight.model.Airport;

/**
 * AirportRepository
 * 空港に関するデータベースとのインターフェース
 * findBy...とJPAのルールに従って記述することにより自動的にメソッドが生成される
 * @Cacheableについては、各キャッシュ保存用のスペースをCachingConfigクラスにて記述している
 */

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {
    @Cacheable("airports")
    public List<Airport> findAll();


    @Cacheable("airports")
    public List<Airport> findByNameLike(String aSearchName);

    public Optional<Airport> findByIataCode(String anairportCode);


}
