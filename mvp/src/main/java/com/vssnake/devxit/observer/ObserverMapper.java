package com.vssnake.devxit.observer;

/**
 * Created by vssnake on 27/02/2017.
 */

public class ObserverMapper {


    public static <T> T map (Class<T> conversion,ObserverEvent observerEvent){
        return conversion.cast(observerEvent.getData());
    }
}
