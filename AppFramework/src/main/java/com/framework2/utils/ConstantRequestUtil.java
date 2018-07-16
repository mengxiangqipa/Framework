package com.framework2.utils;

/**
 * 用于请求一些常量
 *
 * @author Yangjie
 * className ConstantRequestUtil
 * created at  2017/4/18  11:02
 */
public class ConstantRequestUtil {
    private static volatile ConstantRequestUtil singleton;

    private ConstantRequestUtil() {
    }

    public static ConstantRequestUtil getInstance() {
        if (singleton == null) {
            synchronized (ConstantRequestUtil.class) {
                if (singleton == null) {
                    singleton = new ConstantRequestUtil();
                }
            }
        }
        return singleton;
    }

//    public void requestJustShopIds(BaseActivity activity) {
//        requestJustShopIds(activity, null);
//    }
//
//    public void requestJustShopIds(BaseActivity activity, final String errorMsg) {
//        HttpUtil.getInstance().requestJustShopIds(activity, InterfaceConfig.shopIds, new HttpUtil
// .OnRequestListResult<IdsInfo>() {
//            @Override
//            public void onSuccess(List<IdsInfo> list, String... msg) {
//                CustomProgressDialogUtils.dismissProcessDialog();
//                if (!CollectionUtil.getInstance().isEmpty(list)) {
//                    PreferencesHelper.getInstance().putInfo(ConstantsME.currentShopId, list.get(0).getShopId());
//                    PreferencesHelper.getInstance().putInfo(ConstantsME.companyId, list.get(0).getCompanyId());
//                    StringBuilder stringBuilder = new StringBuilder();
//                    for (int i = 0; i < list.size(); i++) {
//                        if (i == 0) {
//                            stringBuilder.append(list.get(i).getShopId());
//                        } else {
//                            stringBuilder.append(",").append(list.get(i).getShopId());
//                        }
//                    }
//                    PreferencesHelper.getInstance().putInfo(ConstantsME.shopIds, stringBuilder.toString());
//                    EventBus.getDefault().post("Success", EventBusTag.getShopIdSuccess);
//                }
//            }
//
//            @Override
//            public void onFail(int code, String msg) {
//                if (!TextUtils.isEmpty(errorMsg)) {
//                    ToastUtil.getInstance().showToast(errorMsg);
//                }
//                CustomProgressDialogUtils.dismissProcessDialog();
//                if (!TextUtils.isEmpty(msg)&&msg.contains("companyId:单位ID不正确")){
//                    EventBus.getDefault().post("Success", EventBusTag.noCompanyId);
//                }
//            }
//        });
//    }
}
