package com.vssnake.devxit.domain.usecase;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

public class DevxitUseCaseFactory {

    private DevxitUseCaseFactory(){}

    private DevxitUseCase devxitUseCase;
    private String nameUseCaseMethod;
    private Object[] params;
    private DevxitUseCaseCallback devxitUseCaseCallback;

    public static DevxitUseCaseFactory createInstance(){return new DevxitUseCaseFactory();}

    public DevxitUseCaseFactory setDevxitUseCase (DevxitUseCase devxitUseCase){
        this.devxitUseCase = devxitUseCase;
        return this;
    }
    public DevxitUseCaseFactory setNameUseCaseMethod(String nameUseCaseMethod){
        this.nameUseCaseMethod = nameUseCaseMethod;
        return this;
    }
    public DevxitUseCaseFactory setParams(Object[] params){
        this.params = params;
        return this;
    }
    public DevxitUseCaseFactory setDevxitUseCaseCallback(DevxitUseCaseCallback callback){
        this.devxitUseCaseCallback = callback;
        return this;
    }
    public DevxitUseCaseWrapper build() {
        params = processParams(params,devxitUseCaseCallback);
        Method useCaseMethod = searchMethod(devxitUseCase, nameUseCaseMethod,params);
        DevxitUseCaseWrapper devxitUseCaseWrapper = new DevxitUseCaseWrapper(devxitUseCase, useCaseMethod, params, devxitUseCaseCallback);
        return devxitUseCaseWrapper;
    }



    private static Method searchMethod(DevxitUseCase devxitUseCase,
                                       String nameUseCaseMethod,
                                       Object[] params)  {
        if (devxitUseCase == null) {
            throw new VerifyError("UseCase not found");
        }
        if (nameUseCaseMethod.isEmpty()) {
            throw new VerifyError("Method not found");
        }
        Method methodFiltered = getMethodFiltered(devxitUseCase,nameUseCaseMethod,params);

        if (!hasCallbackParameter(methodFiltered)){
            throw new VerifyError("The method hasn´t the callback parameter");
        }

        return methodFiltered;



    }

    private static Object[] processParams(Object[] params,
                                          DevxitUseCaseCallback devxitUseCaseCallback){
        if (params == null){
            params = new Object[]{devxitUseCaseCallback};
        }else{
            ArrayList<Object> objectArrayList = new ArrayList<>(Arrays.asList(params));

            objectArrayList.add(devxitUseCaseCallback);
            params = objectArrayList.toArray();
        }

        return params;
    }

    private static Method getMethodFiltered(DevxitUseCase devxitUseCase, String nameUseCaseMethod, Object[] params){
        List<Method> methodsFiltered = getAnnotatedUseCaseMethods(devxitUseCase);

        methodsFiltered = getMethodMatchingName(nameUseCaseMethod, methodsFiltered);
        if (methodsFiltered.isEmpty()) {
            throw new IllegalArgumentException("The target doesn't contain any use case with this name."
                    + "Did you forget to add the @UseCase annotation?");
        }

        methodsFiltered = getMethodMatchingArguments(params, methodsFiltered);
        if (methodsFiltered.isEmpty()) {
            throw new IllegalArgumentException("The target doesn't contain any use case with those args. "
                    + "Did you forget to add the @UseCase annotation?");
        }

        if (methodsFiltered.size() > 1) {
            throw new IllegalArgumentException(
                    "The target contains more than one use case with the same signature. "
                            + "You can use the 'name' property in @UseCase and invoke it with a param name");
        }

        return methodsFiltered.get(0);
    }

    private static List<Method> getAnnotatedUseCaseMethods(Object target) {
        List<Method> useCaseMethods = new ArrayList<>();

        Method[] methods = target.getClass().getMethods();
        for (Method method : methods) {
            UseCase useCaseMethod = method.getAnnotation(UseCase.class);

            if (useCaseMethod != null) {
                useCaseMethods.add(method);
            }
        }
        return useCaseMethods;
    }

    private static List<Method> getMethodMatchingName(String nameUseCase,
                                                      List<Method> methodsFiltered) {
        if (nameUseCase == null || nameUseCase.equals("")) {
            return methodsFiltered;
        }

        Iterator<Method> iteratorMethods = methodsFiltered.iterator();
        while (iteratorMethods.hasNext()) {
            Method method = iteratorMethods.next();

            UseCase annotation = method.getAnnotation(UseCase.class);
            if (!(annotation.name().equals(nameUseCase))) {
                iteratorMethods.remove();
            }
        }

        return methodsFiltered;
    }

    private static List<Method> getMethodMatchingArguments(Object[] selectedArgs,
                                                           List<Method> methodsFiltered) {
        Iterator<Method> iteratorMethods = methodsFiltered.iterator();

        while (iteratorMethods.hasNext()) {
            Method method = iteratorMethods.next();

            Class<?>[] parameters = method.getParameterTypes();

            if (parameters.length == selectedArgs.length) {
                if (!hasValidArguments(method,parameters, selectedArgs)) {
                    iteratorMethods.remove();
                }
            } else {
                iteratorMethods.remove();
            }
        }

        return methodsFiltered;
    }

    private static boolean hasValidArguments(Method method, Class<?>[] parameters, Object[] selectedArgs) {
        for (int i = 0; i < parameters.length; i++) {
            Object argument = selectedArgs[i];
            if (argument != null) {
                Class<?> targetClass;
                if (argument instanceof Class){
                    targetClass = (Class<?>) argument;
                }else{
                    targetClass = argument.getClass();
                }
                Class<?> parameterClass = parameters[i];
                if (!ClassUtils.canAssign(targetClass, parameterClass)) {
                    return false;
                }
                if (i == parameters.length -1){
                    Annotation[] annotations = method.getParameterAnnotations()[parameters.length -1];
                    if (annotations.length != 1
                            || !annotations[0].annotationType().getName()
                            .equals(UseCaseCallback.class.getName())){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean hasCallbackParameter(Method method){
        Annotation[][] annotations = method.getParameterAnnotations();
        if (annotations.length <= 0){
            return false;
        }
        if (annotations[annotations.length -1 ].length <= 0){
            return false;
        }
        Annotation annotation = annotations[annotations.length -1 ][0];
        if (!annotation.annotationType().getName().equals(UseCaseCallback.class.getName())){
            return false;
        }
        return true;

    }
}
