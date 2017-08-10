package com.vssnake.devxit.data;

/**
 * Created by vssnake on 08/02/2017.
 */

public interface JsonConverter {


    <T> T fromJSon(String json, Class<T> tClass) throws Throwable;

    <T> String toJson(T objectToJson) throws  Throwable;
}
