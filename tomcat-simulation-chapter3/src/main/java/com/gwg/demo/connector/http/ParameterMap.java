package com.gwg.demo.connector.http;

import java.util.HashMap;
import java.util.Map;

public final class ParameterMap extends HashMap {

    public ParameterMap(){
        super();
    }

    public ParameterMap(int initialCapacity){
        super(initialCapacity);
    }

    public ParameterMap(int initialCapacity, float loadFactor){
        super(initialCapacity,loadFactor);
    }

    public ParameterMap(Map map){
        super(map);
    }

    private boolean locked = false;
    public boolean isLocked(){
        return this.locked;
    }

    public void setLocked(boolean locked){
        this.locked = locked;
    }




    private static final StringManager sm = StringManager.getManager("com.gwg.demo.connector.http");

    public void clear(){
        if(locked){
            throw new IllegalStateException(sm.getString("ParameterMap.locked"));
        }
        super.clear();
    }

    public Object put(Object key, Object value){
        if(locked){
            throw new IllegalStateException(sm.getString("ParamterMap.locked"));
        }
        return super.put(key, value);
    }

    public void putAll(Map map){
        if(locked){
            throw new IllegalStateException(sm.getString("ParameterMap.locked"));
        }
        super.putAll(map);

    }

    public Object remove(Object key){
        if(locked){
            throw new IllegalStateException(sm.getString("ParamterMap.locked"));
        }
        return super.remove(key);
    }


}
