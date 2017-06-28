package com.vssnake.devxit.data.repository.datasource;

import android.content.Context;
import android.content.SharedPreferences;
import com.vssnake.devxit.data.JsonConverter;
import com.vssnake.devxit.data.exception.ValueNotFoundException;
import com.vssnake.devxit.data.repository.UniqueObject;
import com.vssnake.devxit.utils.LogException;

import java.util.ArrayList;
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

public class SharedPreferencesDataSource<V extends UniqueObject<String>> implements ReadWriteDataSource<String,V> {

    SharedPreferences mSharedPreferences;
    JsonConverter jsonConverter;

    Class<V> classFromInstantiate;

    public SharedPreferencesDataSource(Context appContext,
                                       String preferenceKey,
                                       JsonConverter jsonConverter,
                                        Class<V> classFromInstantiate){
        mSharedPreferences = appContext.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);
        this.jsonConverter = jsonConverter;
        this.classFromInstantiate = classFromInstantiate;
    }


    @Override
    public boolean isValueValid(V value) {
        return isValueExist(value.getKey());
    }

    @Override
    public V getBykey(String key) throws Throwable {
        if (isValueExist(key)){
            return getValueFromSharedPreference(key);
        }else{
            return null;
        }
    }

    @Override
    public Collection<V> getAll() {
        //TODO<
        return null;
    }

    @Override
    public boolean putValue(V value) throws Throwable {
        String serializedValue = serializeValue(value);

        return setValueFromSharedPreference(value.getKey(),serializedValue);
    }

    @Override
    public boolean putOrUpdateValue(V value) throws Throwable {
        String serializedValue = serializeValue(value);


        return setValueFromSharedPreference(value.getKey(),serializedValue);
    }

    @Override
    public Collection<V> putOrUpdateAll(Collection<V> values) {

        Collection<V> savedCollection = new ArrayList<>();

        for (V value : values) {
            boolean savedValue;
            try {
                savedValue = putOrUpdateValue(value);
                if (savedValue) savedCollection.add(value);
            } catch (Throwable throwable) {
                LogException.logException("",throwable);
            }
        }
        return savedCollection;
    }

    @Override
    public boolean deleteByKey(String key) {
        return !isValueExist(key) || removeValueFromSharedPreference(key);
    }


    @Override
    public boolean deleteAll() {
        return mSharedPreferences.edit().clear().commit();
    }

    private boolean isValueExist(String key){
        return mSharedPreferences.contains(key);
    }

    private V getValueFromSharedPreference(String key) throws Throwable {
        if (isValueExist(key)){
            return transformJsonValueToObject(mSharedPreferences.getString(key,null));
        }else{
            throw new ValueNotFoundException();
        }
    }

    private boolean setValueFromSharedPreference(String key, String value){
        return mSharedPreferences.edit().putString(key,value).commit();
    }

    private boolean removeValueFromSharedPreference(String key){
        return  mSharedPreferences.edit().remove(key).commit();
    }


    private V transformJsonValueToObject(String json) throws Throwable {
        if (json != null){
            return (V) jsonConverter.fromJSon(json,classFromInstantiate);

        }else{
            throw new NullPointerException("json is null");
        }
    }

    private String serializeValue(V value) throws Throwable {
        return jsonConverter.toJson(value);
    }
}
