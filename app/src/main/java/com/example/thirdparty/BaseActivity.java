package com.example.thirdparty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;

import com.example.thirdparty.test.L;

import java.lang.reflect.Method;

/**
 * Created by zhuangsj on 16-11-3.
 */

public class BaseActivity extends AppCompatActivity {

    public static final String NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra(NAME);
        if (!TextUtils.isEmpty(title))
            setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.d("onStop",getLocalClassName() + ",isTaskRoot=" + isTaskRoot() + ",isTopOfTask=" + isTopOfTask());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.d("onDestroy",getLocalClassName() + ",isTaskRoot=" + isTaskRoot() + ",isTopOfTask=" + isTopOfTask());
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.d("onPause",getLocalClassName() + ",isTaskRoot=" + isTaskRoot() + ",isTopOfTask=" + isTopOfTask());
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.d("onResume",getLocalClassName() + ",isTaskRoot=" + isTaskRoot() + ",isTopOfTask=" + isTopOfTask());
    }

    @Override
    protected void onStart() {
        super.onStart();
        L.d("onStart",getLocalClassName() + ",isTaskRoot=" + isTaskRoot() + ",isTopOfTask=" + isTopOfTask());
    }

    private boolean isTopOfTask() {
        try {
            Class clazz = getClass();
            while (!clazz.equals(Activity.class)) {
                clazz = clazz.getSuperclass();
            }

            Method method = clazz.getDeclaredMethod("isTopOfTask",new Class[] {});
            method.setAccessible(true);
            return (boolean) method.invoke(this, new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
            L.d("isTopOfTask","isTopOfTask  error = " + e.getMessage());
        }
        return false;
    }

    public void goToActivity(Class<? extends Activity> clazz, String title) {
        Intent intent = new Intent();
        intent.setClass(BaseActivity.this, clazz);
        intent.putExtra(BaseActivity.NAME, title);
        startActivity(intent);
    }
}
