package com.crossfive.framework.handler;

import java.util.ArrayList;
import java.util.List;

public class SameObjectChecker {
    private final List<Object> objs = new ArrayList<Object>(20);
    
    public boolean checkThenPush(Object in){
        if(in == null){
            return true;
        }
        for (Object obj : objs) {
            if(obj == in){
                return false;
            }
        }
        objs.add(in);
        return true;
    }
    
    public Object pop(){
        if(objs.size() == 0){
            return null;
        }
        return objs.remove(objs.size() - 1);
    }
}
