package com.example.thirdparty.model;

import com.example.thirdparty.L;

/**
 * Created by zhuangsj on 17-6-13.
 */

public class SubPerson {
    private String name = "sub";

    public SubPerson(){
        L.d("SubPerson create!!!");
    }

    public SubPerson(String name){
        L.d("SubPerson create " + name);
        this.name = name;
    }

    public SubPerson(Person name){
        L.d("SubPerson create " + name);
        this.name = "father is " + name.getName();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "SubPerson{" +
                "name='" + name + '\'' +
                "} \n " + super.toString();
    }
}
