package com.threshold.rxbus2demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.threshold.rxbus2.RxBus;
import com.threshold.rxbus2demo.bean.DemoBean1;
import com.threshold.rxbus2demo.bean.DemoBean2;
import com.threshold.rxbus2demo.util.RandomUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DemoBean2 demoBean2 =new DemoBean2(String.valueOf(RandomUtil.random(10)));
        RxBus.getDefault().post("222",demoBean2,true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRxBus:
                startActivity(RxBusActivity.class);
                break;
            case R.id.btnBehaviorBus:

                break;
            case R.id.btnReplayBus:

                break;
        }
    }

    private void startActivity(Class<?> activity) {
        startActivity(new Intent(this,activity));
    }
}
