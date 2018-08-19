package com.androidpi.app.items;

import android.support.v4.app.Fragment;

/**
 * Created by jastrelax on 2018/8/19.
 */
public class LiteRefreshSample {

    private String title;
    private String description;
    private Class<? extends Fragment> fragmentClass;

    public LiteRefreshSample(String title, String description, Class<? extends Fragment> fragmentClass) {
        this.title = title;
        this.description = description;
        this.fragmentClass = fragmentClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    public void setFragmentClass(Class<? extends Fragment> fragmentClass) {
        this.fragmentClass = fragmentClass;
    }
}
