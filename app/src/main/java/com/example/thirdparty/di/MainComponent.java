package com.example.thirdparty.di;

import com.example.thirdparty.di.module.PersonModule;
import com.example.thirdparty.model.Person;

import javax.inject.Named;

import dagger.Component;

/**
 * Created by zhuangsj on 17-6-13.
 */

@Component(modules = PersonModule.class)
public interface MainComponent {

//    void inject(MainActivity activity);

    Person providePerson();

    @Named("zhooker")
    Person provideNamedPerson();
}
