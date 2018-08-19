package com.androidpi.app.base.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

/**
 * 一个ViewHolder仅关联一个布局，本身不限制所展示的实体类型，但具体实现类需要知道要展示哪种数据以实现相应的展示逻辑。
 * <p>
 * 它所代表的一个列表项拥有类似一个activity或fragment的作用，可以拥有独立的业务功能，并不仅限于被动的数据展示，
 * 即也可以使用ViewModel实现业务逻辑。
 * <p>
 * 约定优先于配置：尽可能少使用类型参数
 * <p>
 * Created by jastrelax on 2017/8/31.
 */

public abstract class BaseViewHolder<VDB extends ViewDataBinding> extends RecyclerView.ViewHolder
        implements LifecycleOwner {

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    protected VDB binding;
    protected List<Class> dataType = new ArrayList<>();
    protected FragmentManager fragmentManager;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * 根据BaseViewHolder类型和父布局实例化一个BaesViewHolder的子类。
     *
     * @param clazz  BaseViewHolder的实现类。
     * @param parent RecyclerView父布局。
     * @return
     */
    public static BaseViewHolder instance(Class<? extends BaseViewHolder> clazz, ViewGroup parent) {
        if (null == clazz) {
            throw new NullPointerException("The view holder class to be instantiated is null, it may not be " +
                    "registered in RecyclerAdapter or the data types of the objects are not bound with the view holder.");
        }
        BindLayout bindLayout = clazz.getAnnotation(BindLayout.class);
        if (null == bindLayout) {
            throw new NullPointerException("A view holder must be annotated with BindLayout!");
        }
        int layoutId = bindLayout.value();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        try {
            Constructor<? extends BaseViewHolder> constructor = clazz.getDeclaredConstructor(View.class);
            BaseViewHolder viewHolder = constructor.newInstance(itemView);
            viewHolder.binding = DataBindingUtil.bind(itemView);
            viewHolder.dataType = Arrays.asList(bindLayout.dataTypes());
            return viewHolder;
        } catch (NoSuchMethodException e) {
            logError(clazz, e);
        } catch (IllegalAccessException e) {
            Timber.e(e, "%s 实例化错误。", clazz);
        } catch (InvocationTargetException e) {
            Timber.e(e, "%s 实例化错误。", clazz);
        } catch (InstantiationException e) {
            Timber.e(e, "%s 实例化错误。", clazz);
        }
        return null;
    }

    public static BaseViewHolder instance(Class<? extends BaseViewHolder> clazz, ViewGroup parent, FragmentManager fm) {
        BaseViewHolder holder = instance(clazz, parent);
        if (holder != null)
            holder.fragmentManager = fm;
        return holder;
    }

    private static void logError(Class<? extends BaseViewHolder> clazz, NoSuchMethodException e) {
        Timber.e(e, "%s 实例化错误。", clazz);
    }

    /**
     * 解析数据。
     *
     * @param data
     * @param position
     */
    public final void onBindViewHolder(Object data, int position) {
        if (null == data) {
            Timber.w("Data is null!");
            return;
        }
        if (!dataType.contains(data.getClass())) {
            Timber.e("Data type doesn't match the contract!");
            return;
        }
        onBind(data, position);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return this.lifecycleRegistry;
    }

    /**
     * 展示数据。
     */
    public abstract <T> void onBind(T data, int position);

    protected void onAttachedToWindow() {
        // empty
    }

    protected void onDetachedToWindow() {
        // empty
    }

    public void onViewRecycled() {
        // empty
    }
}
