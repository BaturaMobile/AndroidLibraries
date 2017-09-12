package com.vssnake.devxit.domain.usecase.v2;

/**
 * Created by vssnake on 12/09/2017.
 */

import com.vssnake.devxit.domain.usecase.DevxitUseCaseCallback;
import com.vssnake.devxit.exceptions.ErrorBundle;
import com.vssnake.devxit.executor.PostExecutionThread;
import com.vssnake.devxit.executor.ThreadExecutor;

/**
 * Created by Unai Correa on 2017 @vssnake.
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
    public abstract class DevxitUseCase2<T> implements Runnable {

        private ThreadExecutor mThreadExecutor;
        protected PostExecutionThread postExecutionThread;

        DevxitUseCaseCallback<T> devxitUseCaseCallback;

    public DevxitUseCase2(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread){
        this.mThreadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;

    }

    public void execute(){
        if (devxitUseCaseCallback == null){
            throw new IllegalArgumentException("devxitUseCaseCallback is necessary");
        }
        mThreadExecutor.execute(this);
    }

    public void setCallback(DevxitUseCaseCallback<T> devxitUseCaseCallback){
        this.devxitUseCaseCallback = devxitUseCaseCallback;
    }

    protected void setSucess(final T data){
        postExecutionThread.execute(new Runnable() {
            @Override
            public void run() {
                devxitUseCaseCallback.onSuccessCallback(data);
            }
        });

    }

    protected void setError(final ErrorBundle errorBundle){
        postExecutionThread.execute(new Runnable() {
            @Override
            public void run() {
                devxitUseCaseCallback.onErrorException(errorBundle);
            }
        });
    }
}
