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

import com.example.americanflight.service.FlightService;
import com.example.americanflight.service.AirportService;
import com.example.americanflight.type.DtoFlight;
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
 * FlithsApiController
 * APIの制御をおこなうクラス
 * API仕様ごとにRequestMappingにて処理を記述する
 * 処理順序としては以下のような流れとなりクライアントのリクエストを受信する際のユーザー処理の起点
 * 
 * [Client] -> Controller -> Service -> Repository -> [Database]
 */
@RestController
@Validated
@Tag(name = "flights", description = "the flights API")
public class FlightsApiController {

    @Autowired
    FlightService flightService;
    
    @Autowired
    AirportService airportService;

    /**
     * GET /flights
     *
     * @param clientId     (required)
     * @param clientSecret (required)
     * @param destination  (optional)
     * @return (status code 200)
     */

    @Operation(operationId = "flightsGet", responses = {
            @ApiResponse(responseCode = "200", description = "", content = {
                    @Content(mediaType = "*/*", array = @ArraySchema(schema = @Schema(implementation = Object.class)))
            })
    })

    @RequestMapping(method = RequestMethod.GET, path = "/flights")

    ResponseEntity<List<DtoFlight>> flightsGet(
            @NotNull @Parameter(name = "client_id", description = "", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "client_id", required = true) String aClientId,
            @NotNull @Parameter(name = "client_secret", description = "", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "client_secret", required = true) String aCientSecret,
            @Parameter(name = "destination", description = "", in = ParameterIn.QUERY) @RequestParam(value = "destination", required = false) String aDestination) {
	
	// If No airport found, return 400
	// Added for Sceinario 1-II
	if (aDestination != null && !airportService.isAirportExists(aDestination)) {
	    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

        var dtoRes = flightService.getFlights(aDestination);
        return new ResponseEntity<>(dtoRes, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/flights/{ID}")

    public ResponseEntity<DtoFlight> flightsIDGet(
            @Parameter(name = "ID", description = "", required = true, in = ParameterIn.PATH) @PathVariable("ID") @Pattern(regexp = "[0-9]+") String aId,
            @NotNull @Parameter(name = "client_id", description = "", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "client_id", required = true) String clientId,
            @NotNull @Parameter(name = "client_secret", description = "", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "client_secret", required = true) String clientSecret) {
        Optional<DtoFlight> opt = flightService.getFlightById(aId);

        if (opt.isPresent()) {
            DtoFlight dtoFlight = opt.get();
            return new ResponseEntity<DtoFlight>(dtoFlight, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    /**
     * POST /flights
     *
     * @param clientId     (required)
     * @param clientSecret (required)
     * @param generated    (optional)
     * @return (status code 201)
     */
    @Operation(operationId = "flightsPost", responses = {
            @ApiResponse(responseCode = "201", description = "", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            })
    })

    @RequestMapping(method = RequestMethod.POST, value = "/flights", produces = { "application/json" })

    public ResponseEntity<Object> flightsPost(
            @NotNull @Parameter(name = "client_id", description = "", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "client_id", required = true) String clientId,
            @NotNull @Parameter(name = "client_secret", description = "", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "client_secret", required = true) String clientSecret,
            @Valid @RequestBody FlightsPostRequest flightsPostRequest) {

	String originAirport = flightsPostRequest.getOrigin();
	String destAirport = flightsPostRequest.getDestination();

	// Added for Scenario 1-II
	// Check if origin airport exists in Database
	if (!airportService.isAirportExists(originAirport)) {
	    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	// Added for Scenario 1-II
	// Check if dest airport exists in Database
	if (!airportService.isAirportExists(destAirport)) {
	    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}


        Integer id = flightsPostRequest.getID();
        if (id != null) {
            Optional<DtoFlight> dtoFlight = flightService.getFlightById(id.toString());
            // flight record is present.
            if (dtoFlight.isPresent()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        DtoFlight nDtoFlight = DtoFlight.of(flightsPostRequest);
        DtoFlight savedFlight = flightService.saveDtoFlight(nDtoFlight);
        FlightsPostResponse flightsRestResponse = new FlightsPostResponse(savedFlight);

        return new ResponseEntity<>(flightsRestResponse, HttpStatus.CREATED);

    }

    /**
     * PUT /flights/{ID}
     *
     * @param ID           (required)
     * @param clientId     (required)
     * @param clientSecret (required)
     * @param generated    (optional)
     * @return (status code 200)
     */
    @Operation(operationId = "flightsIDPut", responses = {
            @ApiResponse(responseCode = "200", description = "", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            })
    })
    
    @RequestMapping(method = RequestMethod.PUT, value = "/flights/{ID}", produces = { "application/json" })

    public ResponseEntity<Object> flightsIDPut(
            @Parameter(name = "ID", description = "", required = true, in = ParameterIn.PATH) @PathVariable("ID") String aId,
            @NotNull @Parameter(name = "client_id", description = "", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "client_id", required = true) String clientId,
            @NotNull @Parameter(name = "client_secret", description = "", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "client_secret", required = true) String clientSecret,
            @Valid @RequestBody(required = false) FlightsPostRequest flightsPostRequest) {


	String originAirport = flightsPostRequest.getOrigin();
	String destAirport = flightsPostRequest.getDestination();

	// Added for Scenario 1-II
	// Check if origin airport exists in Database
	if (!airportService.isAirportExists(originAirport)) {
	    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	// Added for Scenario 1-II
	// Check if dest airport exists in Database
	if (!airportService.isAirportExists(destAirport)) {
	    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}


        Integer pId = flightsPostRequest.getID();
        if ((pId != null && Integer.parseInt(aId) == pId) || aId != null) {
            Optional<DtoFlight> dtoFlight = flightService.getFlightById(aId);
            if (dtoFlight.isPresent()) {
                // Since it may have no flight id in body, set flight id given by URL params.
                DtoFlight nDtoFlight = DtoFlight.of(flightsPostRequest);
                nDtoFlight.setFlightId(Integer.parseInt(aId));
                DtoFlight savedFlight = flightService.saveDtoFlight(nDtoFlight);
                DtoFlight currentFlight = dtoFlight.get();

                return new ResponseEntity<>(new FlightsPutResponse(currentFlight, savedFlight), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * DELETE /flights/{ID}
     *
     * @param ID           (required)
     * @param clientId     (required)
     * @param clientSecret (required)
     * @return (status code 200)
     */

    @Operation(operationId = "flightsIDDelete", responses = {
            @ApiResponse(responseCode = "200", description = "", content = {
                    @Content(mediaType = "*/*", schema = @Schema(implementation = Object.class))
            })
    })

    @RequestMapping(method = RequestMethod.DELETE, value = "/flights/{ID}", produces = { "*/*" })

    public ResponseEntity<Object> flightsIDDelete(
            @Parameter(name = "ID", description = "", required = true, in = ParameterIn.PATH) @PathVariable("ID") String ID,
            @NotNull @Parameter(name = "client_id", description = "", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "client_id", required = true) String clientId,
            @NotNull @Parameter(name = "client_secret", description = "", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "client_secret", required = true) String clientSecret) {
        flightService.deleteDtoFlight(Integer.parseInt(ID));

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
