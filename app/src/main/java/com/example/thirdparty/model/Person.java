package com.example.thirdparty.model;


import com.example.thirdparty.L;

/**
 * Created by zhuangsj on 17-6-13.
 */

public class Person {

    private String name = "init";

    public Person(){
        L.d("person create!!!");
    }

    public Person(String name){
        L.d("person create " + name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                "} \n " + super.toString();
    }
}
