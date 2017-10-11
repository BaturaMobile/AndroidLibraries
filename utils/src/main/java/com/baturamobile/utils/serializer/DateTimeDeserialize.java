package com.baturamobile.utils.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.DateTime;

import java.lang.reflect.Type;

/**
 * Created by vssnake on 11/10/2017.
 */

public class DateTimeDeserialize implements JsonDeserializer<DateTime>{

    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        return new DateTime(json.getAsJsonPrimitive().getAsString());
    }
}
