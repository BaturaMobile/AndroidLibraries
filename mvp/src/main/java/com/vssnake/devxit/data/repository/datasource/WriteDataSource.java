package com.vssnake.devxit.data.repository.datasource;

import com.vssnake.devxit.data.repository.UniqueObject;

import java.util.Collection;

/**
 */

public interface WriteDataSource<K, V extends UniqueObject<K>> {

    boolean putValue(V value) throws Throwable;

    boolean putOrUpdateValue(V value) throws Throwable;

    Collection<V> putOrUpdateAll(Collection<V> value);

    boolean deleteByKey(K key);

    boolean deleteAll();
}
