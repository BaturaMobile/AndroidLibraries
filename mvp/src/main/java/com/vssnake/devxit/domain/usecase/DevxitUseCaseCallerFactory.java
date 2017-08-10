package com.vssnake.devxit.domain.usecase;


import android.util.Log;

import com.vssnake.devxit.exceptions.ErrorBundle;
import com.vssnake.devxit.executor.PostExecutionThread;
import com.vssnake.devxit.executor.ThreadExecutor;

import java.lang.ref.WeakReference;

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

public class DevxitUseCaseCallerFactory {


    private static final String TAG = DevxitUseCaseCallerFactory.class.getSimpleName();

    protected static ThreadExecutor threadExecutor;

    protected static PostExecutionThread postExecutionThread;




    public static DevxitUseCaseWrapper createWrapper(DevxitUseCaseFactory devxitUseCaseFactory, DevxitUseCaseCallback devxitUseCaseCallback){

        devxitUseCaseFactory.setDevxitUseCaseCallback(devxitUseCaseCallback);

        return devxitUseCaseFactory.build();
    }




    @Inject
    public  DevxitUseCaseCallerFactory(ThreadExecutor threadExecutor,PostExecutionThread postExecutionThread){
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }



    public <T> DevxitUseCaseCaller build(DevxitUseCaseFactory devxitUseCaseFactory,
                                         Class<T> clazz){

        return DevxitUseCaseCaller.createCaller(clazz,devxitUseCaseFactory);
    }


    public static class DevxitUseCaseCaller<T>{

        private static <T> DevxitUseCaseCaller createCaller(Class<T> clazz,
                                                            DevxitUseCaseFactory devxitUseCaseFactory){
            DevxitUseCaseCaller<T> devxitUseCaseCaller = new DevxitUseCaseCaller<>();
            devxitUseCaseFactory.setDevxitUseCaseCallback(devxitUseCaseCaller.useCaseCallback);

            devxitUseCaseCaller.devxitUseCaseWrapper = devxitUseCaseFactory.build();


            return devxitUseCaseCaller;

        }

        public DevxitUseCaseWrapper devxitUseCaseWrapper;

        private WeakReference<DevxitUseCaseCallback<T>> useCaseCallbackWeakReference;



        public  DevxitUseCaseCallback useCaseCallback = new DevxitUseCaseCallback<T>() {
            @Override
            public void onErrorException(final ErrorBundle errorBundle) {

                if (useCaseCallbackWeakReference != null &&
                        useCaseCallbackWeakReference.get() != null){

                    postExecutionThread.execute(new Runnable() {
                        @Override
                        public void run() {

                            useCaseCallbackWeakReference.get().onErrorException(errorBundle);

                        }
                    });
                }
            }

            @Override
            public void onSuccessCallback(final T result) {

                Log.d(TAG,"onSuccessCallback | " + useCaseCallbackWeakReference.get());
                if (useCaseCallbackWeakReference != null &&
                        useCaseCallbackWeakReference.get() != null){

                    postExecutionThread.execute(new Runnable() {
                        @Override
                        public void run() {

                            useCaseCallbackWeakReference.get().onSuccessCallback(result);

                        }
                    });
                }
            }
        };

        public void setCallback(DevxitUseCaseCallback<T> devxitUseCaseCallback){

            Log.d(TAG,"onsetCallback | " + devxitUseCaseCallback);
            this.useCaseCallbackWeakReference = new WeakReference<>(devxitUseCaseCallback);
        }

        public void setDelayParams(Object... params) throws Exception {
            if (devxitUseCaseWrapper != null){
                devxitUseCaseWrapper.setParamsDelay(params);
            }
        }

        public void execute(){
            if (devxitUseCaseWrapper != null){
                threadExecutor.execute(devxitUseCaseWrapper);
            }else{
                throw new NoClassDefFoundError("Wrapper not initialized");
            }

        }


    }






}
