package com.example.amelicanflight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.amelicanflight.model.Plane;

/**
 * PlaneRepository
 * 航空機に関するデータベースとのインターフェース
 * findBy...とJPAのルールに従って記述することにより自動的にメソッドが生成される
 */

@Repository
public interface PlaneRepository extends JpaRepository<Plane, Integer> {
    public List<Plane> findByPlaneTypeAndTotalSeats(String aPlaneType, Integer aTotalSeats);
}
