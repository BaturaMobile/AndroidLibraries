package com.vssnake.devxit.data.repository.datasource;

import com.vssnake.devxit.data.repository.UniqueObject;

import java.util.Collection;

/**
 */

public class EmptyReadDataSource <K,V extends UniqueObject<K>> implements  ReadDataSource<K,V> {
    @Override
    public V getBykey(K key) throws Throwable {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> getAll() {
        throw new UnsupportedOperationException();
    }
}
