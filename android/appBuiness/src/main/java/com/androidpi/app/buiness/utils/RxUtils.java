package com.androidpi.app.buiness.utils;

import org.reactivestreams.Publisher;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.MaybeTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {

    public static <U> SingleTransformer<U, U> networkIO() {
        return new SingleTransformer<U, U>() {
            @Override
            public SingleSource<U> apply(Single<U> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <U> SingleTransformer<U, U> diskIO() {
        return new SingleTransformer<U, U>() {
            @Override
            public SingleSource<U> apply(Single<U> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <U>MaybeTransformer<U, U> maybeNetworkIO() {
        return upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
    }

    public static <U>MaybeTransformer<U, U> maybeDiskIO() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

    public static <U> ObservableTransformer<U, U> observableNetworkIO() {
        return (upstream) -> {
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        };
    }

    public static <U> ObservableTransformer<U, U> observableDiskIO() {
        return (upstream) -> {
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        };
    }

    public static CompletableTransformer completableNetworkIO() {
        return new CompletableTransformer() {
            @Override
            public CompletableSource apply(Completable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static CompletableTransformer completableDiskIO() {
        return new CompletableTransformer() {
            @Override
            public CompletableSource apply(Completable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <U> FlowableTransformer<U, U> flowableNetworkIO() {
        return new FlowableTransformer<U, U>() {
            @Override
            public Publisher<U> apply(Flowable<U> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}