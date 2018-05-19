package cn.androidpi.app;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/4/27.
 */
public class RxJavaTest {

    @Test
    public void testErrorHandling() {
        Observable.just("")
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        return Observable.just("")
                                .flatMap(new Function<String, ObservableSource<String>>() {
                                    @Override
                                    public ObservableSource<String> apply(String s) throws Exception {
                                        throw new NullPointerException("exception0-1 for test");
                                    }
                                })
                                .flatMap(new Function<String, ObservableSource<String>>() {
                                    @Override
                                    public ObservableSource<String> apply(String s) throws Exception {
                                        throw new NullPointerException("exception0 for test");
                                    }
                                }, true);
                    }
                }, true)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        return Observable.just("test");
                    }
                }, true)
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("doOnError0");
                        throwable.printStackTrace();
                    }
                })
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        throw new NullPointerException("exception1 for test");
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("doOnError1");
                        throwable.printStackTrace();
                    }
                })
                .flatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(String s) throws Exception {
                        throw new NullPointerException("exception2 for test");
                    }
                })
                .flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Object o) throws Exception {
                        return Observable.just("");
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("doOnError2");
                        throwable.printStackTrace();
                    }
                })
                .onErrorReturn(new Function<Throwable, Object>() {
                    @Override
                    public Object apply(Throwable throwable) throws Exception {
                        return "error handling";
                    }
                })
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object value) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                        System.out.println(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
