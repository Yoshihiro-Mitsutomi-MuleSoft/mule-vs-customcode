package com.example.amelicanflight.util;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * BigDecimalSerializer
 * BigDecimalのシリアライズにおいて使用
 * JSONへの変換において、整数が小数点込みでシリアライズされてしまう部分を回避する目的
 */
public class BigDecimalSerializer extends StdSerializer<BigDecimal> {

  public BigDecimalSerializer() {
    this(null);
  }

  public BigDecimalSerializer(Class<BigDecimal> aClass) {
    super(aClass);
  }

  @Override
  public void serialize(BigDecimal aBigDecimal, JsonGenerator aGenerator, SerializerProvider aProvider)
      throws IOException {
    aGenerator.writeNumber(new BigDecimal(aBigDecimal.stripTrailingZeros().toPlainString()));
  }
}
