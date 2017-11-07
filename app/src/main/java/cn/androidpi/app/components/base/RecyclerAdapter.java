package cn.androidpi.app.components.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 列表适配器，一个列表适配的列表项展示什么内容只与BaseViewHolder的实现类有关。
 *
 * 一个BaseViewHolder类包含了所需要的布局信息，数据类型信息，RecyclerAdapter负责将负载数据项
 * 映射到一种BaseViewHolder的子类。
 *
 * Created by jastrelax on 2017/8/30.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder>{

    private final SparseIntArray mDataViewMap = new SparseIntArray();
    private final SparseArray<Class<? extends BaseViewHolder>> mViewHolderMap= new SparseArray<>();
    private final List<Object> mPayloads = new ArrayList<>();

    /**
     * 注册一个BaseViewHolder以用于数据展示。
     * @param clazz
     */
    public RecyclerAdapter register(Class<? extends BaseViewHolder> clazz) {
        BindLayout bindLayout = clazz.getAnnotation(BindLayout.class);
        // 建立数据类型到布局的映射
        for (Class dataType : bindLayout.dataTypes()) {
            mDataViewMap.append(dataType.hashCode(), bindLayout.value());
        }
        // 建立布局到BaseViewHolder的映射
        mViewHolderMap.put(bindLayout.value(), clazz);

        return this;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Class<? extends BaseViewHolder> viewHolderClass = mViewHolderMap.get(viewType);
        return BaseViewHolder.instantiate(viewHolderClass, parent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Object item = mPayloads.get(position);
        holder.resolve(item);
    }

    @Override
    public int getItemCount() {
        return mPayloads.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mPayloads.get(position);
        return mDataViewMap.get(item.getClass().hashCode());
    }

    public void setPayloads(Collection<?> mPayloads) {
        if (null == mPayloads) {
            // TODO: 2017/9/1 数据为空的占位
            return;
        }
        this.mPayloads.clear();
        this.mPayloads.addAll(mPayloads);
        notifyDataSetChanged();
    }
}
