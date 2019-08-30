package com.gwg.demo.startup.server2;


public class StaticResourceProcessor {

    public void process(Request request, Response response){
        try {
            response.sendStaticResouce();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
