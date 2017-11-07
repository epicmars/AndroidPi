package cn.androidpi.app.components.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jastrelax on 2017/9/8.
 */

public class BaseFragment<VDB extends ViewDataBinding> extends Fragment {

    protected VDB mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BindLayout bindLayout = getClass().getAnnotation(BindLayout.class);
        if (null == bindLayout) {
            // A fragment without view.
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        mBinding = DataBindingUtil.inflate(inflater, bindLayout.value(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.unbind();
    }
}
