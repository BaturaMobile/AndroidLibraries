package com.vssnake.devxit.domain.usecase;

import android.content.Context;

import javax.inject.Inject;

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

public class TestCase extends DevxitUseCase {

    Context context;

    @Inject
    public TestCase(Context context){
        this.context = context;

    }

    @UseCase(name = "testCase")
    public void methodWhitName(@UseCaseCallback DevxitUseCaseCallback<String> devxitUseCaseCallback){
        devxitUseCaseCallback.onSuccessCallback("hola");
    }
}
