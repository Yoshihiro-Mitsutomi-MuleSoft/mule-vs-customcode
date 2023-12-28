package com.example.amelicanflight.type;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * FlightsPostRequest
 * フライトの登録(POST)のリクエストで使用
 * OpenAPI Code generatorで生成されたコードを一部修正 (Validation部分等)
 */

public class FlightsPostRequest {

  private Integer ID;

  private String code;

  private BigDecimal price;

  private String departureDate;

  private String origin;

  private String destination;

  private Integer emptySeats;

  @Valid
  private Map<String, Object> plane = new HashMap<>();

  public FlightsPostRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FlightsPostRequest(String code, BigDecimal price, String departureDate, String origin, String destination,
      Integer emptySeats) {
    this.code = code;
    this.price = price;
    this.departureDate = departureDate;
    this.origin = origin;
    this.destination = destination;
    this.emptySeats = emptySeats;
  }

  public FlightsPostRequest ID(Integer ID) {
    this.ID = ID;
    return this;
  }

  /**
   * Get ID
   * 
   * @return ID
   */

  @Schema(name = "ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ID")
  public Integer getID() {
    return ID;
  }

  public void setID(Integer ID) {
    this.ID = ID;
  }

  public FlightsPostRequest code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * 
   * @return code
   */
  @NotNull
  @Schema(name = "code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public FlightsPostRequest price(BigDecimal price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * 
   * @return price
   */
  @NotNull
  @Valid
  @Schema(name = "price", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("price")
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public FlightsPostRequest departureDate(String departureDate) {
    this.departureDate = departureDate;
    return this;
  }

  /**
   * Get departureDate
   * 
   * @return departureDate
   */
  @NotNull
  @Schema(name = "departureDate", requiredMode = Schema.RequiredMode.REQUIRED)
  @Pattern(regexp = "[0-9]{1,4}/[0-9]{1,2}/[0-9]{1,2}")
  @JsonProperty("departureDate")
  public String getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(String departureDate) {
    this.departureDate = departureDate;
  }

  public FlightsPostRequest origin(String origin) {
    this.origin = origin;
    return this;
  }

  /**
   * Get origin
   * 
   * @return origin
   */
  @NotNull
  @Schema(name = "origin", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("origin")
  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public FlightsPostRequest destination(String destination) {
    this.destination = destination;
    return this;
  }

  /**
   * Get destination
   * 
   * @return destination
   */
  @NotNull
  @Schema(name = "destination", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("destination")
  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public FlightsPostRequest emptySeats(Integer emptySeats) {
    this.emptySeats = emptySeats;
    return this;
  }

  /**
   * Get emptySeats
   * 
   * @return emptySeats
   */
  @NotNull
  @Schema(name = "emptySeats", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("emptySeats")
  public Integer getEmptySeats() {
    return emptySeats;
  }

  public void setEmptySeats(Integer emptySeats) {
    this.emptySeats = emptySeats;
  }

  public FlightsPostRequest plane(Map<String, Object> plane) {
    this.plane = plane;
    return this;
  }

  public FlightsPostRequest putPlaneItem(String key, Object planeItem) {
    if (this.plane == null) {
      this.plane = new HashMap<>();
    }
    this.plane.put(key, planeItem);
    return this;
  }

  /**
   * Get plane
   * 
   * @return plane
   */

  @Schema(name = "plane", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("plane")
  public Map<String, Object> getPlane() {
    return plane;
  }

  public void setPlane(Map<String, Object> plane) {
    this.plane = plane;
  }

  /**
   * A container for additional, undeclared properties.
   * This is a holder for any undeclared properties as specified with
   * the 'additionalProperties' keyword in the OAS document.
   */
  private Map<String, Object> additionalProperties;

  /**
   * Set the additional (undeclared) property with the specified name and value.
   * If the property does not already exist, create it otherwise replace it.
   */
  @JsonAnySetter
  public FlightsPostRequest putAdditionalProperty(String key, Object value) {
    if (this.additionalProperties == null) {
      this.additionalProperties = new HashMap<String, Object>();
    }
    this.additionalProperties.put(key, value);
    return this;
  }

  /**
   * Return the additional (undeclared) property.
   */
  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  /**
   * Return the additional (undeclared) property with the specified name.
   */
  public Object getAdditionalProperty(String key) {
    if (this.additionalProperties == null) {
      return null;
    }
    return this.additionalProperties.get(key);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FlightsPostRequest flightsPostRequest = (FlightsPostRequest) o;
    return Objects.equals(this.ID, flightsPostRequest.ID) &&
        Objects.equals(this.code, flightsPostRequest.code) &&
        Objects.equals(this.price, flightsPostRequest.price) &&
        Objects.equals(this.departureDate, flightsPostRequest.departureDate) &&
        Objects.equals(this.origin, flightsPostRequest.origin) &&
        Objects.equals(this.destination, flightsPostRequest.destination) &&
        Objects.equals(this.emptySeats, flightsPostRequest.emptySeats) &&
        Objects.equals(this.plane, flightsPostRequest.plane) &&
        Objects.equals(this.additionalProperties, flightsPostRequest.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ID, code, price, departureDate, origin, destination, emptySeats, plane, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FlightsPostRequest {\n");
    sb.append("    ID: ").append(toIndentedString(ID)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    departureDate: ").append(toIndentedString(departureDate)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
    sb.append("    destination: ").append(toIndentedString(destination)).append("\n");
    sb.append("    emptySeats: ").append(toIndentedString(emptySeats)).append("\n");
    sb.append("    plane: ").append(toIndentedString(plane)).append("\n");

    sb.append("    additionalProperties: ").append(toIndentedString(additionalProperties)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
