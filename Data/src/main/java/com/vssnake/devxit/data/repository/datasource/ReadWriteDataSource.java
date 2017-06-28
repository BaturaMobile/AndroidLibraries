package com.vssnake.devxit.data.repository.datasource;

import com.vssnake.devxit.data.repository.UniqueObject;

/**
 */

public interface ReadWriteDataSource<K,V extends UniqueObject<K>>
        extends ReadDataSource<K,V>,WriteDataSource<K,V> {

    boolean isValueValid(V value);
}
