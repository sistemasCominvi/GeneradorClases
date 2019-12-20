package com.cominvi.app.commons.util;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
/*
comentario dummys
*/
public class CustomCalendarDeserializer extends JsonDeserializer<Date> implements ContextualDeserializer {

	private String formatKey;

	public CustomCalendarDeserializer() {
		super();
	}

	public CustomCalendarDeserializer(String key) {
		super();
		this.formatKey = key;
	}

	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		String dateAsString = jsonparser.getText();
		try {
			Date date = UtilDate.formatStringToDate(dateAsString, this.formatKey);
			return date;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
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
		return new CustomCalendarDeserializer(key);
	}

}
