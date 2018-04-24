package com.lee.ultrascan.service;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {
    private static final Subject<Object, Object> INSTANCE = new SerializedSubject(PublishSubject.create());

    public static void publish(Object event) {
        INSTANCE.onNext(event);
    }

    public static <T> Observable<T> observe(Class<T> eventType) {
        return INSTANCE.ofType(eventType);
    }
}
