package com.cominvi.app.commons.util;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Date;

public class CustomDateSerializer extends StdSerializer<Date> implements ContextualSerializer {


  private String formatKey;

  public CustomDateSerializer() {
    super(Date.class);
  }

  public CustomDateSerializer(String key) {
    super(Date.class);
    this.formatKey = key;
  }

  @Override
  public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2) throws IOException {
    if (value != null) {
      try {
        String formattedDate = UtilDate.getFormatByFecha(value, formatKey);
        gen.writeString(formattedDate.toUpperCase());
      } catch (Exception e) {
        gen.writeNull();
      }
    } else {
      gen.writeNull();
    }
  }

  public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
      throws JsonMappingException {
    String key = null;
    JsonDate ann = null;

    if (property != null) {
      ann = property.getAnnotation(JsonDate.class);
    }

    if (ann != null) {
      key = ann.formatKey();
    }

    if (key == null) {
      key = UtilDate.FORMAT_STANDAR_DATE;
    }
    return new CustomDateSerializer(key);
  }
}
