package com.threshold.rxbus2.bean;

import java.util.EventObject;

import io.reactivex.annotations.NonNull;

import static io.reactivex.annotations.SchedulerSupport.NONE;

/**
 * All event send by RxBus2 should derived this class.
 * Created by threshold on 2018/1/24.
 */
@SuppressWarnings("WeakerAccess")
public class RxEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    private String tag;
    private Object source;

    public RxEvent(String tag, @NonNull Object source) {
        super(source);
        this.tag = tag;
        this.source = source;

    }

    public RxEvent(@NonNull Object source) {
        super(source);
        this.tag = NONE;
        this.source = source;

    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "RxEvent{" +
                "tag='" + tag + '\'' +
                ", source=" + source +
                '}';
    }
}
