package com.demo.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

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
