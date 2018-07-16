package com.demo.entity;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

/**
 * dataBinding 的item项
 *
 * @author Yangjie
 * className DataBindingItem
 * created at  2016/12/21  10:17
 */

public class DataBindingItem<T> extends BaseObservable {
    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> content = new ObservableField<>();
}
