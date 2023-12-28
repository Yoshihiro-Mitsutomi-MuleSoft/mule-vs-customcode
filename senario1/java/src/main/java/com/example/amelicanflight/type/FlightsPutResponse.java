package com.example.amelicanflight.type;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * FlightsPutResponse
 * フライトの登録(PUT)のレスポンスで使用
 * PUTのリクエストについてはPOSTで使用しているFlightsPostRequestを使用
 */

@Getter
@Setter
public class FlightsPutResponse {
    List<String> updatedKeys;
    DtoFlight before;
    DtoFlight after;

    public FlightsPutResponse(DtoFlight aBefore, DtoFlight aAfter) {
        Map<String, Object> bm = aBefore.asMap();
        Map<String, Object> am = aAfter.asMap();
        updatedKeys = am.keySet().stream().filter(k -> !(bm.get(k).equals(am.get(k)))).collect(Collectors.toList());
        before = aBefore;
        after = aAfter;
    }
}
