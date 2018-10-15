package com.threshold.rxbus2.bean;

import java.util.EventObject;
import io.reactivex.annotations.NonNull;

/**
 * All event send by RxBus2 should derived this class.
 * Created by threshold on 2018/1/24.
 */
@SuppressWarnings("WeakerAccess")
public  class RxEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    private String eventId;
    private Object source;

    public RxEvent(String eventId,@NonNull Object source) {
        super(source);
        this.eventId =eventId;
        this.source=source;

    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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
                "eventId='" + eventId + '\'' +
                ", source=" + source +
                '}';
    }
}
