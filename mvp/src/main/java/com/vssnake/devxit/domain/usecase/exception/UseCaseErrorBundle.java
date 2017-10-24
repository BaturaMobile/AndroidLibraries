package com.vssnake.devxit.domain.usecase.exception;

import com.vssnake.devxit.exceptions.ErrorBundle;

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

public class UseCaseErrorBundle implements ErrorBundle {

    private final Throwable exception;

    public UseCaseErrorBundle(Throwable exception){
        this.exception = exception;
    }

    @Override
    public Throwable getException() {
        return exception;
    }

    @Override
    public String getErrorMessage() {

        String message = "";
        if (this.exception != null){
            message += this.exception.getMessage();
        }
        return message;
    }
}
