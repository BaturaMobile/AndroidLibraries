package com.vssnake.devxit.data.repository.policy;

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

public class ReadPolicy {

    private final int policy;
    private static final int CACHE = 0;
    private static final int READ = 1;
    private static final int ALL = 2;

    private ReadPolicy(int policyType){
        this.policy = policyType;
    }

    public static ReadPolicy CACHE_ONLY = new ReadPolicy(CACHE);
    public static ReadPolicy READ_ONLY = new ReadPolicy(READ);
    public static ReadPolicy READ_ALL = new ReadPolicy(ALL);

    public boolean useCache() {
        return policy == CACHE || policy == ALL;
    }

    public boolean useReadable() {
        return policy == READ || policy == ALL;
    }



}
