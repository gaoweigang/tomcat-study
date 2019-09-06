package com.gwg.demo.connector.http;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取参数
 * 在调用javax.servlet.http.HttpServletRequest的getParameter(),getParameterMap(),getParameterNames()或getParameterValues()方法之前，都不需要解析查询
 * 字符串或HTTP请求体来获得参数。因此，在HttpRequest类中，这4个方法的实现都会先调用parseParameter()方法
 *
 * 参数只需要解析一次即可，而且也只会解析一次，因为，在请求体中包含参数，解析参数的工作会使SocketInputStream类读完整个字节流。HttpRequest类会使用一个名为parsed
 * 的布尔变量来标识是否已经完成对参数的解析。
 *
 * 注意：servlet程序员不可以修改参数值。因此，这里使用了一个特殊的HashMap类：org.apache.catalina.util.ParameterMap
 *
 * ParameterMap类继承自java.util.HashMap,其中有一个名为locked的布尔变量。只有当变量locked值为false时，才可以对ParameterMap对象中的名/值对进行添加，更新或者删除操作。
 * 否则，会抛出IllegalStateException异常。
 *
 */
public final class ParameterMap extends HashMap {

    //---------------------------------构造函数
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


    //------------------------------------锁，如果已锁住则不能再修改参数的值
    private boolean locked = false;
    public boolean isLocked(){
        return this.locked;
    }

    public void setLocked(boolean locked){
        this.locked = locked;
    }



    //------------------------------------对HashMap所有的修改方法简单的重写，限制不能随便修改参数
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
