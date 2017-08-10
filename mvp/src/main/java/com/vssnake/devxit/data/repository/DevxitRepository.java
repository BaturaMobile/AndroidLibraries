package com.vssnake.devxit.data.repository;

import android.support.annotation.Nullable;


import com.vssnake.devxit.data.repository.datasource.ReadWriteDataSource;
import com.vssnake.devxit.data.repository.datasource.ReadDataSource;
import com.vssnake.devxit.data.repository.datasource.WriteDataSource;
import com.vssnake.devxit.data.repository.policy.ReadPolicy;
import com.vssnake.devxit.data.repository.policy.WritePolicy;

import java.util.Collection;

/**
 * Created by Unai Correa on 2016 @vssnake.
 * <p>
 * This file is part of some open source application.
 * <p>
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * <p>
 * Email : unai.correa.cruz@gmail.com
 */

public class DevxitRepository<K,V extends UniqueObject<K>>
    implements ReadDataSource<K,V>, WriteDataSource<K,V>{

    private final ReadDataSource<K,V> readDataSource;
    private final WriteDataSource<K,V> writeDataSource;
    private final ReadWriteDataSource<K,V> cacheDataSource;

    public DevxitRepository(@Nullable ReadDataSource<K,V> readDataSource,
                            @Nullable WriteDataSource<K,V> writeDataSource,
                            @Nullable ReadWriteDataSource<K,V> cacheDataSource){
        this.readDataSource = readDataSource;
        this.writeDataSource = writeDataSource;
        this.cacheDataSource = cacheDataSource;
    }

    @Override
    public V getBykey(K key) throws Throwable {
        return getByKey(key, ReadPolicy.READ_ALL);
    }

    public V getByKey(K key, ReadPolicy policy) throws Throwable {
        validateKey(key);

        V valueToRead = null;

        if (policy.useCache()){
            valueToRead = getValueFromCache(key);
        }

        if (policy.useReadable()){
            valueToRead = getValueFromReadDataSource(key);
        }
        if (valueToRead != null){
            populateCache(valueToRead);
        }

        return valueToRead;
    }



    @Override
    public Collection<V> getAll() throws Throwable {
        return getAll(ReadPolicy.READ_ALL);
    }

    public Collection<V> getAll(ReadPolicy policy) throws Throwable {
        Collection<V> valuesToRead = null;

        if (policy.useCache()){
            valuesToRead = getValuesFromCache();
        }

        if (policy.useReadable()){
            valuesToRead = getValuesFromReadDataSource();
        }
        if (valuesToRead != null){
            populateCache(valuesToRead);
        }

        return valuesToRead;
    }


    @Override
    public boolean putValue(V value) throws Throwable {
        return putOrUpdate(value, WritePolicy.ADD);
    }



    @Override
    public boolean putOrUpdateValue(V value) throws Throwable {
        return putOrUpdate(value,WritePolicy.ADD_OR_UPDATE);
    }

    @Override
    public Collection<V> putOrUpdateAll(Collection<V> value) {
        return putOrUpdate(value);
    }



    @Override
    public boolean deleteByKey(K key) {
        boolean deleted = false;
        if (writeDataSource != null){
            deleted = writeDataSource.deleteByKey(key);
        }
        if (cacheDataSource != null){
            cacheDataSource.deleteByKey(key);
        }
        return deleted;
    }

    @Override
    public boolean deleteAll() {
        boolean deleted = false;
        if (writeDataSource != null){
            deleted = writeDataSource.deleteAll();
        }
        if (cacheDataSource != null){
            cacheDataSource.deleteAll();
        }
        return deleted;
    }

    private void populateCache(V valueToRead) throws Throwable {
        assert cacheDataSource != null;
        cacheDataSource.putOrUpdateValue(valueToRead);
    }

    private Collection<V> getValuesFromReadDataSource() throws Throwable {
        assert readDataSource != null;
        return readDataSource.getAll();
    }

    private Collection<V> getValuesFromCache() throws Throwable {
        if (cacheDataSource != null){
            return cacheDataSource.getAll();
        }
        return  null;

    }

    private V getValueFromReadDataSource(K key) throws Throwable {
        if (readDataSource != null){
            return readDataSource.getBykey(key);
        }
        return null;
    }

    private V getValueFromCache(K key) throws Throwable {
        if (cacheDataSource != null) {
            return cacheDataSource.getBykey(key);
        }
        return null;
    }


    private boolean putOrUpdate(V value, WritePolicy addOrUpdate) throws Throwable {
        validateValue(value);
        assert writeDataSource != null;

        boolean valueIsAdded;
        if (addOrUpdate.isAddOnly()){
            valueIsAdded =  writeDataSource.putValue(value);
        }else{
            valueIsAdded =  writeDataSource.putOrUpdateValue(value);
        }

        if (valueIsAdded){
            populateCache(value);
        }

        return valueIsAdded;
    }

    private void populateCache(Collection<V> valuesToRead) {
        if (cacheDataSource != null){
            cacheDataSource.putOrUpdateAll(valuesToRead);
        }

    }

    private Collection<V> putOrUpdate(Collection<V> values) {
        assert  writeDataSource != null;

        Collection<V> valuesAdded = writeDataSource.putOrUpdateAll(values);

        if (valuesAdded != null && cacheDataSource != null){
            cacheDataSource.putOrUpdateAll(valuesAdded);
        }
        return valuesAdded;
    }

    private void validateKey(K key){
        if (key == null)
            throw new IllegalArgumentException("The key used can´ be null");
    }

    private void validateValue(V value){
        if (value == null)
            throw new IllegalArgumentException("The value cannot be null");
    }
}
