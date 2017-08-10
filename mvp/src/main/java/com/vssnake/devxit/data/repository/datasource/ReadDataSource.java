package com.vssnake.devxit.data.repository.datasource;

import java.util.Collection;

/**
 */

public interface ReadDataSource<K,V> {

    V getBykey(K key) throws Throwable;

    Collection<V> getAll() throws Throwable;
}
