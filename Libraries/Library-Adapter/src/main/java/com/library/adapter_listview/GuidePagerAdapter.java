//package com.library.adapter_listview;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.support.v4.view.PagerAdapter;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//
//import java.text.DecimalFormat;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//
//public class GuidePagerAdapter extends PagerAdapter {
//    private Context context;
//    private List<GalleryInfo> list;
//    private final int[] array;
//
//    public GuidePagerAdapter(Context context, List<GalleryInfo> list) {
//        this.context = context;
//        this.list = list;
//        array = new int[]{R.mipmap.icon_tao, R.mipmap.icon_su, R.mipmap.ic_launcher_round};
//    }
//
//    @Override
//    public int getCount() {
//        return list == null ? 0 : list.size() <= 1 ? list.size() : Integer.MAX_VALUE;
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
//        View view = LayoutInflater.from(context).inflate(R.layout.item_gallery, null);
//        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
//        TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
//        TextView tv_phone_name = (TextView) view.findViewById(R.id.tv_phone_name);
//        TextView tv_total_sale = (TextView) view.findViewById(R.id.tv_total_sale);
//        TextView tv_store_count = (TextView) view.findViewById(R.id.tv_store_count);
//        TextView tv_shop_name_left = (TextView) view.findViewById(R.id.tv_shop_name_left);
//        TextView tv_shop_name_right = (TextView) view.findViewById(R.id.tv_shop_name_right);
//
//        LinearLayout leftCompareLayout = (LinearLayout) view.findViewById(R.id.leftCompareLayout);
//        LinearLayout rightCompareLayout = (LinearLayout) view.findViewById(R.id.rightCompareLayout);
//        ImageView iv_compare_left = (ImageView) view.findViewById(R.id.iv_compare_left);
//        ImageView iv_compare_right = (ImageView) view.findViewById(R.id.iv_compare_right);
//        TextView tv_compare_left = (TextView) view.findViewById(R.id.tv_compare_left);
//        TextView tv_compare_right = (TextView) view.findViewById(R.id.tv_compare_right);
//
//        tv_title.setText(String.valueOf("销量Top" + (position % list.size() + 1)));
//        tv_count.setText(String.valueOf("已售:" + list.get(position % list.size()).getAlreadySaleCount() + "台"));
//        tv_phone_name.setText(String.valueOf(list.get(position % list.size()).getPhoneName()));
//        decimalFormat.applyPattern("0.00");
//        tv_total_sale.setText(String.valueOf(decimalFormat.format(list.get(position % list.size()).getLastTotalSale
// ()) + "元"));
//        tv_store_count.setText(String.valueOf(list.get(position % list.size()).getStoreCount() + "台"));
//        tv_shop_name_left.setText(String.valueOf("京东售价"));
//        tv_shop_name_right.setText(String.valueOf("本店售价"));
//        tv_compare_left.setText(String.valueOf("京东比价"));
//        tv_compare_right.setText(String.valueOf("淘宝比价"));
//        iv_compare_left.setImageResource(array[position % list.size() % array.length]);
//        iv_compare_right.setImageResource(array[(position % list.size() + 1) % array.length]);
//        leftCompareLayout.setOnClickListener(new BaseOnClickListener() {
//            @Override
//            protected void onBaseClick(View v) {
//                ToastUtil.getInstance().showToast("点击比价1");
//                notifyDataSetChanged();
//            }
//        });
//        rightCompareLayout.setOnClickListener(new BaseOnClickListener() {
//            @Override
//            protected void onBaseClick(View v) {
//                ToastUtil.getInstance().showToast("点击比价2");
//                notifyDataSetChanged();
//            }
//        });
//
//        SimpleLineChart mSimpleLineChart = (SimpleLineChart) view.findViewById(R.id.simpleLineChart);
//        Calendar calendar = Calendar.getInstance();
//        decimalFormat.applyPattern("00");
//        String[] xItem = new String[5];
//        String[] yItem = {"", "", "", "", ""};
//        for (int i = 0; i < xItem.length; i++) {
//            String month = decimalFormat.format(calendar.get(Calendar.MONTH));
//            String day = decimalFormat.format(calendar.get(Calendar.DAY_OF_MONTH) + 1);
//            xItem[xItem.length - i - 1] = String.valueOf(month + "-" + day);
//            calendar.add(Calendar.DATE, -1);//减少一天
//        }
//
//        mSimpleLineChart.setXItem(xItem);
//        mSimpleLineChart.setYItem(yItem);
//        //点线1-- 数据
//        HashMap<Integer, Integer> pointMap1 = new HashMap();
//        for (int i = 0; i < xItem.length; i++) {
//            pointMap1.put(i, (int) (Math.random() * 5));
//        }
//        //点线2-- 数据
//        HashMap<Integer, Integer> pointMap2 = new HashMap();
//        for (int i = 0; i < xItem.length; i++) {
//            pointMap2.put(i, (int) (Math.random() * 5));
//        }
//        mSimpleLineChart.setLine1Color(Color.parseColor("#FF0000"));
//        mSimpleLineChart.setLine2Color(Color.parseColor("#3290e8"));
//        mSimpleLineChart.setData1(pointMap1);
//        mSimpleLineChart.setData2(pointMap2);
//
//        container.addView(view);
//        return view;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
//    }
//}
