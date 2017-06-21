package com.example.thirdparty.di;

import com.example.thirdparty.MainActivity;
import com.example.thirdparty.di.module.PersonModule;
import com.example.thirdparty.di.module.SubPersonModule;
import com.example.thirdparty.model.SubPerson;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by zhuangsj on 17-6-13.
 */

@Singleton
@Component(modules = SubPersonModule.class , dependencies = MainComponent.class)
public interface SubComponent {

    void inject(MainActivity activity);

}
