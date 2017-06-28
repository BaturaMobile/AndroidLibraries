package com.vssnake.devxit.observer;

import com.vssnake.devxit.executor.PostExecutionThread;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by vssnake on 27/02/2017.
 */

public class ObserverController {


    WeakHashMap<String,WeakHashMap<ObserverInterface,WeakReference<ObserverInterface>>> weakHashMapHashMap;

    PostExecutionThread postExecutionThread;

    public ObserverController(PostExecutionThread postExecutionThread){
        weakHashMapHashMap = new WeakHashMap<>();
        this.postExecutionThread = postExecutionThread;
    }


    public boolean observe(String key, ObserverInterface observerInterface ){
        if (!weakHashMapHashMap.containsKey(key)){
            createWeakHashMap(key);
        }
        weakHashMapHashMap.get(key).put(observerInterface,new WeakReference<>(observerInterface));

        return true;
    }

    public void emit(String key, Object object){
        if (weakHashMapHashMap.containsKey(key)){
            emitData(weakHashMapHashMap.get(key),key,object);
        }
    }

    public boolean removeObserver(String key,ObserverInterface observerInterface){
        if (weakHashMapHashMap.containsKey(key)){
            weakHashMapHashMap.get(key).remove(observerInterface);
            return true;
        }
        return false;
    }

    private void emitData(WeakHashMap<ObserverInterface,WeakReference<ObserverInterface>> collectionObservers,
                          final String key,
                          final Object data){

        for (Map.Entry<ObserverInterface, WeakReference<ObserverInterface>>
                observerInterfaceObserverInterfaceEntry : collectionObservers.entrySet()) {
            if(observerInterfaceObserverInterfaceEntry.getValue().get() != null){
                postExecutionThread.execute(new Runnable() {
                    ObserverInterface observerInterface;
                    @Override
                    public void run() {
                        observerInterface.onEvent(key, data);
                    }
                    Runnable init(ObserverInterface observerInterface) {
                        this.observerInterface=observerInterface;
                        return(this);
                    }
                }.init(observerInterfaceObserverInterfaceEntry.getValue().get()));

            }

        }
    }

    private WeakHashMap<ObserverInterface,WeakReference<ObserverInterface>> createWeakHashMap (String key){
        return weakHashMapHashMap.put(key,new WeakHashMap<ObserverInterface,WeakReference<ObserverInterface>>());
    }
}
