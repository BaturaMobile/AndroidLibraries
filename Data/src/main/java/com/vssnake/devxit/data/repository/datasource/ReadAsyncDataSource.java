package com.vssnake.devxit.data.repository.datasource;

import java.util.Collection;

/**
 * Created by vssnake on 30/03/2017.
 */

public interface ReadAsyncDataSource<K,V> {


    void getBykey(K key, DevxitGetValueAsync<V> valueAsync);

    void getAll(DevxitGetValuesAsync<V> devxitGetValuesAsync);

    interface DevxitGetValueAsync<V>{
        void onResult(V value);
        void onFail(Throwable error);
    }
    interface DevxitGetValuesAsync<V>{
        void onResult(Collection<V> value);
        void onFail(Throwable error);
    }
}
