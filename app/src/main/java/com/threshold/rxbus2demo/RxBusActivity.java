package com.threshold.rxbus2demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.threshold.rxbus2.RxBus;
import com.threshold.rxbus2.annotation.RxSubscribe;
import com.threshold.rxbus2.bean.RxEvent;
import com.threshold.rxbus2.util.EventThread;
import com.threshold.rxbus2demo.bean.DemoBean1;
import com.threshold.rxbus2demo.bean.DemoBean2;
import com.threshold.rxbus2demo.util.RandomUtil;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Demo for showing {@link RxBus} usage.
 * Created by threshold on 2017/1/18.
 */

public class RxBusActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("RxBus");
        setContentView(R.layout.activity_rx_bus);

        textView = findViewById(R.id.text);
        //clear sticky event if you need.
//        RxBus.getDefault().clearSticky();
        //auto register listen event.
        RxBus.getDefault().register(this);
        //manual listen event
//        Disposable subscribe = RxBus.getDefault()
//                .ofStickyType(String.class)
//                .observeOn(Schedulers.io())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        manualListenRxEvent("First Subscriber",s);
//                    }
//                });
//        mCompositeDisposable.add(subscribe);
    }


    @RxSubscribe(observeOnThread = EventThread.MAIN, tag = "111")
    @SuppressWarnings("unused")
    public void autoListenRxEvent(DemoBean1 demoBean1) {
        String text = String.format("{autoListenRxEvent Receive Normal DemoEvent1: %s\nThreadId: %s }\n", demoBean1.getData(), Thread.currentThread().getId());
        Logger.d(text);
        textView.append(text);
        textView.append("\n");
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN,isSticky = true, tag = "111")
    @SuppressWarnings("unused")
    public void autoListenRxEventStick(DemoBean1 demoBean1) {
        String text = String.format("{autoListenRxEvent Receive Stick DemoEvent1: %s\nThreadId: %s }\n", demoBean1.getData(), Thread.currentThread().getId());
        Logger.d(text);
        textView.append(text);
        textView.append("\n");
    }



    @RxSubscribe(observeOnThread = EventThread.IO,  tag = "222")
    @SuppressWarnings("unused")
    private void autoListenRxEvent2(DemoBean2 demoBean2) {
        final String text = String.format("{autoListenRxEvent2 Receive Normal DemoEvent2: %s\nThreadId: %s }\n", demoBean2.getData(), Thread.currentThread().getId());
        Logger.d(text);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(text);
                textView.append("\n");
            }
        });
    }

    //now we support private method.
    @RxSubscribe(observeOnThread = EventThread.IO, isSticky = true, tag = "222")
    @SuppressWarnings("unused")
    private void autoListenRxEvent2Stick(DemoBean2 demoBean2) {
        final String text = String.format("{autoListenRxEvent2 Receive Sticky DemoEvent2: %s\nThreadId: %s }\n", demoBean2.getData(), Thread.currentThread().getId());
        Logger.d(text);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(text);
                textView.append("\n");
            }
        });
    }



    // Will crash on register. Because no param in method.
//    @RxSubscribe(observeOnThread = EventThread.IO, isSticky = true)
//    @SuppressWarnings("unused")
//    private void autoListenRxEvent3() {
//
//    }

    // Will crash on register. Because two param in method. We expect ONLY ONE param.
//    @RxSubscribe(observeOnThread = EventThread.IO, isSticky = true)
//    @SuppressWarnings("unused")
//    private void autoListenRxEvent3(DemoEvent1 event1,DemoEvent2 event2) {
//
//    }

    // Will crash on receive event. Because you shouldn't update view state on BackgroundThread.
    //You should update UI(View) on MAIN THREAD(UI THREAD).
//    @RxSubscribe(observeOnThread = EventThread.IO, isSticky = true) @SuppressWarnings("unused")
//    private void autoListenRxEvent3(DemoEvent2 event) {
//        final String text = String.format("{autoListenRxEvent2 Receive sticky event: %s\nThreadId: %s }\n", event.getDemoBean2().getData(), Thread.currentThread().getId());
//        Logger.d(text);
//        textView.append(text);//will crash on here.
//        textView.append("\n");
//    }


    private void manualListenRxEvent(RxEvent event) {
        final String text = String.format("{manualListenRxEvent [Receive DemoEvent1]: %s}\n", event.getSource().toString());
        Logger.d(text);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(text);
                textView.append("\n");
            }
        });
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnFireEvent:
                DemoBean1 demoBean1 = new DemoBean1(String.valueOf(RandomUtil.random(10)));
                RxBus.getDefault().post("111", demoBean1,false);
                break;
            case R.id.btnFireStickyEvent:
                DemoBean1 demoBean3 = new DemoBean1(String.valueOf(RandomUtil.random(10)));
                DemoBean2 demoBean2 = new DemoBean2(RandomUtil.random(10));
                RxBus.getDefault().post("222", demoBean2,true);
                break;
            case R.id.btnAddNewSubscriber:
                Disposable subscribe = RxBus.getDefault()
                        .ofStickyType(DemoBean2.class)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(new Consumer <Object>() {
                            @Override
                            public void accept(Object event1) throws Exception {
                                manualListenRxEvent((RxEvent) event1);

                            }
                        });
                mCompositeDisposable.add(subscribe);
                view.setEnabled(false);
                break;
            case R.id.btnRemoveStickyEvent:
                List <DemoBean2> stickies = RxBus.getDefault()
                        .getSticky(DemoBean2.class);
                if (stickies != null && stickies.size() > 0) {
                    RxBus.getDefault().removeStickyEventAt(DemoBean2.class, stickies.size() - 1);//remove the last sticky event
                    textView.append("Already removed last sticky event, you can press back key and reenter this activity and see difference.\n");
                } else {
                    textView.append("No sticky event found, please fire some sticky event first\n");
                }
                break;
        }

//        List<Integer> integers = new ArrayList<>();
//        integers.add(1);
//        integers.add(2);
//        integers.add(3);
//        RxBus.getDefault().post(integers);
//        RxBus.getDefault().post(new String[]{"Str1","Str2","Str3"});
//        RxBus.getDefault().post(new int[]{1,2,3});
//        RxBus.getDefault().post('C');
//        RxBus.getDefault().post(0.05f);
//        RxBus.getDefault().post(13.56);
//        RxBus.getDefault().post(Long.MAX_VALUE);
//        RxBus.getDefault().post(Byte.MIN_VALUE);
//        RxBus.getDefault().post(Boolean.FALSE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // manual listen event should release by yourself.
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        //auto release register with Annotation RxSubscribe.
        RxBus.getDefault().unregister(this);
    }

}
