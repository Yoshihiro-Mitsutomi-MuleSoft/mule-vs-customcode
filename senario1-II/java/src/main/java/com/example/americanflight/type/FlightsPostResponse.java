package com.example.americanflight.type;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * FlightsPostResponse
 * フライトの登録(POST)のレスポンスで使用
 */

@Getter
@Setter
public class FlightsPostResponse {
    @Schema(name = "ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("ID")
    Integer ID;

    public FlightsPostResponse(DtoFlight aDtoFlight) {
        ID = aDtoFlight.getFlightId();
    }
}
