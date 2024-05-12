package com.example.americanflight;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.americanflight.service.AirportService;
import com.example.americanflight.type.DtoAirport;
import com.example.americanflight.type.DtoAirportDetail;
import com.example.americanflight.type.FlightsPostRequest;
import com.example.americanflight.type.FlightsPostResponse;
import com.example.americanflight.type.FlightsPutResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * AirportsApiController
 * APIの制御をおこなうクラス
 * API仕様ごとにRequestMappingにて処理を記述する
 * 処理順序としては以下のような流れとなりクライアントのリクエストを受信する際のユーザー処理の起点
 * 
 * [Client] -> Controller -> Service -> Repository -> [Database]
 */
@RestController
@Validated
@Tag(name = "airports", description = "the airpotrs API")
public class AirportsApiController {

    @Autowired
    AirportService airportService;

    /**
     * GET /airports
     *
     * @param ID           (required)
     * @param clientId     (required)
     * @param clientSecret (required)
     * @param search       (option)
     * @return (status code 200)
     */

    @Operation(operationId = "airportsGet", responses = {
            @ApiResponse(responseCode = "200", description = "", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            })
    })

    @RequestMapping(method = RequestMethod.GET, value = "/airports", produces = { "application/json" })

    ResponseEntity<List<DtoAirport>> airportsGet(
            @NotNull @Parameter(name = "client_id", description = "", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "client_id", required = true) String aClientId,
            @NotNull @Parameter(name = "client_secret", description = "", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "client_secret", required = true) String aCientSecret,
            @RequestParam(value = "search", required = false) String aSearchName) {

        List<DtoAirport> dtoRes = airportService.getAirports(aSearchName);

        if (dtoRes.isEmpty() && aSearchName != null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(dtoRes, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/airports/{airportCode}")

    public ResponseEntity<DtoAirportDetail> airportsCodeGet(
            @NotNull @Parameter(name = "client_id", description = "", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "client_id", required = true) String clientId,
            @NotNull @Parameter(name = "client_secret", description = "", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "client_secret", required = true) String clientSecret,
            @Parameter(name = "airportCode", description = "", required = true, in = ParameterIn.PATH) @PathVariable("airportCode") String airportCode) {

        Optional<DtoAirportDetail> opt = airportService.getAirportDetail(airportCode);

        if (opt.isPresent()) {
            DtoAirportDetail dtoAirportDetail = opt.get();
            return new ResponseEntity<DtoAirportDetail>(dtoAirportDetail, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
