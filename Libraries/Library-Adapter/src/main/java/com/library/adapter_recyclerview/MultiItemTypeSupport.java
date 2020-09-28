package com.library.adapter_recyclerview;

import androidx.annotation.LayoutRes;

/**
 * recyclerView--adapter的多类型type
 *
 * @author YobertJomi
 * className MultiItemTypeSupport
 * created at  2016/12/26  17:49
 */
public interface MultiItemTypeSupport<T> {
    /*获取类型，0,1,2,3，*/
    int getMultiItemViewType(int position, T t);

    /*根据类型(getItemViewType返回值)返回layoutId*/
    @LayoutRes
    int getLayoutId(int itemType);
}
