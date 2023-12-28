package com.example.amelicanflight.type;

import com.example.amelicanflight.model.Plane;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DtoPlane
 * 内部で使用するためのPlane用のDTOクラス
 * 
 */

@Getter
@Setter
public class DtoPlane extends Convertable {
    @NotNull
    String planeType;
    @NotNull
    Integer totalSeats;

    public DtoPlane() {
    }

    public Plane toPlane() {
        return new Plane(planeType, totalSeats);
    }
}
