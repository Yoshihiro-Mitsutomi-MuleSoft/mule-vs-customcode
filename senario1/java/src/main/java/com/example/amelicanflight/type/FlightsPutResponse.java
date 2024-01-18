package com.example.amelicanflight.type;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
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

    public void getDiffs(Map<String, Object> aMap, Map<String, Object> bMap, List<String> path, List<String> updateKeys) {

        aMap.forEach((key, value) -> {
            if(HashMap.class.isInstance(value)) {
                path.add(key);
                getDiffs((Map<String, Object>)value, bMap, path, updateKeys);
                path.remove(path.size() - 1);
            } else {
              Map<String, Object> ref = bMap;
              for (String p : path) {
                ref = (Map<String, Object>)ref.get(p);
              }
              if (!(value.equals(ref.get(key)))) {
                path.add(key);
                updateKeys.add(String.join(".", path));
                path.remove(path.size() - 1);
              }
            }
        });
    }
    public FlightsPutResponse(DtoFlight aBefore, DtoFlight aAfter) {
        Map<String, Object> bm = aBefore.asMap();
        Map<String, Object> am = aAfter.asMap();
        updatedKeys = new ArrayList();
        List<String> path = new ArrayList<String>();
        getDiffs(am, bm, path, updatedKeys);
        before = aBefore;
        after = aAfter;
       
    }
}
