package com.vssnake.devxit.domain.usecase;

import com.vssnake.devxit.domain.usecase.exception.UseCaseErrorBundle;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

public class DevxitUseCaseWrapper implements Runnable {

    private final DevxitUseCase devxitUseCase;
    private final Method method;
    private Object[] params;
    private final WeakReference<DevxitUseCaseCallback> devxitUseCaseCallbackWeakReference;

    public DevxitUseCaseWrapper(DevxitUseCase devxitUseCase, Method method, Object[] params,
                                DevxitUseCaseCallback devxitUseCaseCallback){
        this.devxitUseCase = devxitUseCase;
        this.method = method;
        this.params = params;
        this.devxitUseCaseCallbackWeakReference
                = new WeakReference<>(devxitUseCaseCallback);
    }

    @Override
    public void run() {
        try {
            method.invoke(devxitUseCase,params);
        } catch (IllegalAccessException e) {
            launchException(e);
        } catch (InvocationTargetException e) {
            launchException(e);
        }
    }

    public void setParamsDelay(Object... paramsDelay) throws Exception {
        //this +1 is because this params not included the callback
        if (params.length != paramsDelay.length + 1){
            throw new Exception("params are not the same");
        }
        for (int x = 0 ; paramsDelay.length > x ; x++){
            if (paramsDelay[x].getClass().equals(params[x])) {
                params[x] = paramsDelay[x];
            }else if (paramsDelay[x].getClass().getInterfaces().length > 0){
                for (Class<?> clazz : paramsDelay[x].getClass().getInterfaces()){
                    if (clazz.isInstance(paramsDelay[x])){
                        params[x] = paramsDelay[x];
                        break;
                    }

                }
                if (!params[x].equals(paramsDelay[x])){
                    throw new Exception("instance params are not the same");
                }
            }else{
                throw new Exception("instance params are not the same");
            }
        }
    }



    private void launchException(Exception e){
        if (devxitUseCaseCallbackWeakReference.get() != null){
            devxitUseCaseCallbackWeakReference.get().onErrorException(new UseCaseErrorBundle(e));
        }
    }
}
