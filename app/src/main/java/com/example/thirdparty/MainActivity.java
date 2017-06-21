package com.example.thirdparty;

import android.os.Bundle;
import android.view.View;

import com.example.thirdparty.di.DaggerMainComponent;
import com.example.thirdparty.di.DaggerSubComponent;
import com.example.thirdparty.model.Person;
import com.example.thirdparty.model.SubPerson;

import javax.inject.Inject;
import javax.inject.Named;

public class MainActivity extends BaseLogActivity {

    @Inject
    public Person mPerson1;

    @Inject
    @Named("zhooker")
    public Person mPerson2;

    @Inject
    public SubPerson mSubPerson1;

    @Inject
    public SubPerson mSubPerson2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        DaggerMainComponent.builder().build().inject(this);
        DaggerSubComponent.builder().mainComponent(DaggerMainComponent.builder().build()).build().inject(this);

        clearProcess();
        addActionButton("创建实例", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();
                updateProcess("mPerson1 = " + mPerson1);
                updateProcess("mPerson2 = " + mPerson2 + "\n");
            }
        });

        addActionButton("获取单例", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();
                updateProcess("mSubPerson1 = " + mSubPerson1);
                updateProcess("mSubPerson2 = " + mSubPerson2 + "\n");
            }
        });
    }
}
