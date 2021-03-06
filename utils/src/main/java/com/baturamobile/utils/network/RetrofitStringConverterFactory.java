package com.baturamobile.utils.network;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by vssnake on 12/06/2017.
 */

public final class RetrofitStringConverterFactory extends Converter.Factory {
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new Converter<ResponseBody, String>() {
            @Override public String convert(@NonNull ResponseBody value) throws IOException {
                return value.string();
            }
        };
    }

    @Override public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                                    Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new Converter<String, RequestBody>() {
            @Override public RequestBody convert(@NonNull String value) throws IOException {
                return RequestBody.create(MediaType.parse("text/plain"), value);
            }
        };
    }

    public static RetrofitStringConverterFactory create() {
        return new RetrofitStringConverterFactory();
    }
}
