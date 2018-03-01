package com.baturamobile.utils.network;

import retrofit2.Response;

/**
 * Created by vssnake on 22/12/2017.
 */

public class RetrofitEndPointChecker {

    public static <T> void ResponseChecker(Response<T> response) throws NetworkException {

        if (!response.isSuccessful()){
            throw new NetworkException(response.code());
        }
    }
}
