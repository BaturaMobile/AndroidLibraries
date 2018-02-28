package com.baturamobile.utils.network;

import retrofit2.Response;

/**
 * Created by vssnake on 10/10/2017.
 */

@Deprecated
public class NetWorkUtils {

    public static NetWorkException createError(Response response){
        try{
            return new NetWorkException(response.errorBody().string());
        }catch (Exception e){
            return new NetWorkException(e.getMessage());
        }

    }
}
