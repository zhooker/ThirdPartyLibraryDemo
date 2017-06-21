package com.example.thirdparty.di.module;

import com.example.thirdparty.model.Person;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhuangsj on 17-6-13.
 */

@Module
public class PersonModule {

    @Provides
    public Person providePerson() {
        return new Person();
    }

    @Provides
    @Named("zhooker")
    public Person provideNamedPerson() {
        return new Person("zhooker");
    }
}
