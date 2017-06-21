package com.example.thirdparty.di.module;

import com.example.thirdparty.model.Person;
import com.example.thirdparty.model.SubPerson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhuangsj on 17-6-13.
 */

@Module
public class SubPersonModule {

    @Provides
    @Singleton
    public SubPerson provideSubPerson(Person person) {
        return new SubPerson(person);
    }
}
