package com.vssnake.devxit.data.repository.datasource;

import com.vssnake.devxit.data.repository.UniqueObject;

import java.util.Collection;

/**
 */

public class EmptyCacheDataSource<K,V extends UniqueObject<K>> implements ReadWriteDataSource<K,V> {
    @Override
    public boolean isValueValid(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V getBykey(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean putValue(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean putOrUpdateValue(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> putOrUpdateAll(Collection<V> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteByKey(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteAll() {
        throw new UnsupportedOperationException();
    }
}
