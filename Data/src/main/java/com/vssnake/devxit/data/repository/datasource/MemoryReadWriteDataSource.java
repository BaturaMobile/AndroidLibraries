package com.vssnake.devxit.data.repository.datasource;

import com.vssnake.devxit.data.repository.UniqueObject;
import com.vssnake.devxit.time.TimeProvider;
import com.vssnake.devxit.utils.LogException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

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

public class MemoryReadWriteDataSource<K,V extends UniqueObject<K>>
    implements ReadWriteDataSource<K,V> {

    protected final Map<K,V> itemsMap;
    protected final long timeToLiveMilliseconds;
    protected final TimeProvider timeProvider;

    protected long lastItemUpdate;

    public MemoryReadWriteDataSource(TimeProvider timeProvider, long timeToLiveMilliseconds){
        itemsMap = new LinkedHashMap<>();
        this.timeProvider = timeProvider;
        this.timeToLiveMilliseconds = timeToLiveMilliseconds;
    }

    @Override
    public boolean isValueValid(V value) {
        return (getCurrentTime() - lastItemUpdate) < timeToLiveMilliseconds;
    }

    @Override
    public V getBykey(K key) {
        return itemsMap.get(key);
    }

    @Override
    public Collection<V> getAll() {
        return new ArrayList<>(itemsMap.values());
    }

    @Override
    public boolean putValue(V value) {
        if (value == null) throw new NullPointerException("Value is null");
        if (itemsMap.containsKey(value.getKey()))
            return false;
        else{
            lastItemUpdate = getCurrentTime();
            itemsMap.put(value.getKey(),value);
            return true;
        }


    }

    @Override
    public boolean putOrUpdateValue(V value) {
        if (value == null) throw new NullPointerException("Value is null");
        lastItemUpdate = getCurrentTime();
        V savedValue =  itemsMap.put(value.getKey(),value);
        return savedValue != null;
    }

    @Override
    public Collection<V> putOrUpdateAll(Collection<V> values) {
        if (values == null) throw new NullPointerException("Values is null");

        Collection<V> savedCollection = new ArrayList<>();

        for (V currentValue : values) {
            boolean savedValue;
            try {
                savedValue = putOrUpdateValue(currentValue);
                if (savedValue) savedCollection.add(currentValue);
            } catch (Throwable throwable) {
                LogException.logException("",throwable);
            }
        }
        return savedCollection;
    }

    @Override
    public boolean deleteByKey(K key) {
        if (key == null) throw new NullPointerException("key is null");
        return  itemsMap.remove(key)!= null;
    }

    @Override
    public boolean deleteAll() {
        itemsMap.clear();
        lastItemUpdate = 0;
        return true;
    }

    private long getCurrentTime(){
        return timeProvider.currentTimeMillis();
    }


}
