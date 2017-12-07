package com.example.thirdparty;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class BaseLogActivity extends BaseActivity {

    protected static final String TAG = "zhuangsj";
    private TextView mProcess;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        container = (LinearLayout) findViewById(R.id.btn_container);
        mProcess = (TextView) findViewById(R.id.process);
    }

    protected void updateProcess(final String process) {
        Log.d(TAG, "updateProcess: " + process);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String origin = mProcess.getText().toString();
                mProcess.setText(process + "\n" + origin);
            }
        });
    }

    protected void clearProcess() {
        mProcess.setText("");
    }

    protected void addActionButton(String title, View.OnClickListener listener) {
        Button btn = createButton();
        btn.setText(title);
        btn.setOnClickListener(listener);
        this.container.addView(btn);
    }

    protected Button createButton() {
        Button btn = new Button(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        btn.setAllCaps(false);
        btn.setLineSpacing(1.2f, 1.2f);
        btn.setPadding(5, 12, 5, 12);
        return btn;
    }

    protected void insertSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int temp = a[i];
            int j = i;
            for (; j > 0 && a[j - 1] > temp; j--) {
                a[j] = a[j - 1];
            }
            a[j] = temp;
        }
    }
}
