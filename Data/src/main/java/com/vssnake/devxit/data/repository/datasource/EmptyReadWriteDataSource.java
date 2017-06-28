package com.vssnake.devxit.data.repository.datasource;

import com.vssnake.devxit.data.repository.UniqueObject;

import java.util.Collection;

/**
 * Created by vssnake on 27/02/2017.
 */

public class EmptyReadWriteDataSource<K,V extends UniqueObject<K>> implements  ReadWriteDataSource<K,V> {
    @Override
    public V getBykey(K key) throws Throwable {
        return null;
    }

    @Override
    public Collection<V> getAll() throws Throwable {
        return null;
    }

    @Override
    public boolean putValue(V value) throws Throwable {
        return false;
    }

    @Override
    public boolean putOrUpdateValue(V value) throws Throwable {
        return false;
    }

    @Override
    public Collection<V> putOrUpdateAll(Collection<V> value) {
        return null;
    }

    @Override
    public boolean deleteByKey(K key) {
        return false;
    }

    @Override
    public boolean deleteAll() {
        return false;
    }

    @Override
    public boolean isValueValid(V value) {
        return false;
    }
}
