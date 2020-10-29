package com.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.activity.BaseActivity;
import com.demo.enums.StateEnum;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import custom.org.greenrobot.eventbus.EventBus;
import custom.org.greenrobot.eventbus.Subscribe;
import custom.org.greenrobot.eventbus.ThreadMode;

/**
 * Fragment基类
 *
 * @author YobertJomi
 * className BaseFragment
 * created at  2020/10/29  11:57
 */
public abstract class BaseFragment extends Fragment {

    protected Unbinder unbinder;

    private StateEnum netStateEnum = StateEnum.NONE;

    /**
     * 页面是否准备好
     */
    private boolean isPrepared;

    /**
     * eventBus通知新消息
     *
     * @param string String
     */
    @Subscribe(threadMode = ThreadMode.MAIN, tag = "eventBusT")
    public void notifyMethod(String string) {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = initContentView(inflater, container, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        initLocalData(savedInstanceState);
        initUi(savedInstanceState);
        initLocalDataAfterUi(savedInstanceState);
        initListener();
        requestNetData();
    }

    /**
     * 初始化碎片布局
     *
     * @param inflater           LayoutInflater
     * @param container          ViewGroup
     * @param savedInstanceState Bundle
     * @return 碎片View对象
     */
    public abstract View initContentView(LayoutInflater inflater, ViewGroup container,
                                         Bundle savedInstanceState);

    /**
     * 初始化本地数据，intent传递，数据库等(初始化UI后)
     *
     * @param savedInstanceState @Nullable Bundle
     */
    protected void initLocalDataAfterUi(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 初始化本地数据，intent传递，数据库等
     *
     * @param savedInstanceState @Nullable Bundle
     */
    protected void initLocalData(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 初始化Ui
     *
     * @param savedInstanceState @Nullable Bundle
     */
    protected abstract void initUi(@Nullable Bundle savedInstanceState);

    /**
     * 初始化事件监听
     */
    public void initListener() {
    }

    /**
     * 请求网络数据
     */
    public void requestNetData() {
        netStateEnum = StateEnum.DOING;
    }

    public void setNetStateEnum(StateEnum netStateEnum) {
        this.netStateEnum = netStateEnum;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (netStateEnum == StateEnum.FAIL) {
            requestNetData();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isPrepared = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isPrepared = false;
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    public final WeakReference<BaseActivity> getWeakReference() {
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getWeakReference();
        }
        return null;
    }

    public final BaseActivity getActivityM() {
        if (getWeakReference() != null) {
            return getWeakReference().get();
        }
        return null;
    }

    public boolean isPrepared() {
        return isPrepared;
    }
}
