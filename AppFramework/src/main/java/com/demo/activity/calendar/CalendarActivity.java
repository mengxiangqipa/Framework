package com.demo.activity.calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.demo.R;
import com.framework.widget.CustomCalendar;

import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends Activity {

    String date = null;// 设置默认选中的日期  格式为 “2014-04-05” 标准DATE格式

    Button bt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.calendar_activity);
        bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new PopupWindows(CalendarActivity.this, bt);
            }
        });
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.calendar_popupwindow_calendar,
                    null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.calendar_fade_in));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.calendar_push_bottom_in_1));

            setWidth(LayoutParams.FILL_PARENT);
            setHeight(LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            final TextView popupwindow_calendar_month = (TextView) view
                    .findViewById(R.id.popupwindow_calendar_month);
            final CustomCalendar calendar = (CustomCalendar) view
                    .findViewById(R.id.popupwindow_calendar);
            Button popupwindow_calendar_bt_enter = (Button) view
                    .findViewById(R.id.popupwindow_calendar_bt_enter);

            popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
                    + calendar.getCalendarMonth() + "月");

            if (null != date) {

                int years = Integer.parseInt(date.substring(0,
                        date.indexOf("-")));
                int month = Integer.parseInt(date.substring(
                        date.indexOf("-") + 1, date.lastIndexOf("-")));
                popupwindow_calendar_month.setText(years + "年" + month + "月");

                calendar.showCalendar(years, month);
                calendar.setCalendarDayBgColor(date, R.drawable.calendar_date_focused);
            }

            List<String> list = new ArrayList<String>(); //设置标记列表
            list.add("2014-04-01");
            list.add("2014-04-02");
            calendar.addMarks(list, 0);
//			calendar.setHasNextMonth(false);
            //监听所选中的日期
            calendar.setOnCalendarClickListener(new CustomCalendar.OnCalendarClickListener() {

                public void onCalendarClick(int row, int col, String dateFormat) {
                    int month = Integer.parseInt(dateFormat.substring(
                            dateFormat.indexOf("-") + 1,
                            dateFormat.lastIndexOf("-")));

                    if (calendar.getCalendarMonth() - month == 1//跨年跳转
                            || calendar.getCalendarMonth() - month == -11) {
                        calendar.lastMonth();
                    } else if (month - calendar.getCalendarMonth() == 1 //跨年跳转
                            || month - calendar.getCalendarMonth() == -11) {
                        calendar.nextMonth();
                    } else {
                        calendar.removeAllBgColor();
                        calendar.setCalendarDayBgColor(dateFormat,
                                R.drawable.calendar_date_focused);
                        date = dateFormat;//最后返回给全局 date
                    }
                }
            });

            //监听当前月份
            calendar.setOnCalendarDateChangedListener(new CustomCalendar.OnCalendarDateChangedListener() {
                public void onCalendarDateChanged(int year, int month) {
                    popupwindow_calendar_month
                            .setText(year + "年" + month + "月");
                }
            });

            //上月监听按钮
            RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) view
                    .findViewById(R.id.popupwindow_calendar_last_month);
            popupwindow_calendar_last_month
                    .setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            calendar.lastMonth();
                        }
                    });

            //下月监听按钮
            RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) view
                    .findViewById(R.id.popupwindow_calendar_next_month);
            popupwindow_calendar_next_month
                    .setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            calendar.nextMonth();
                        }
                    });

            //关闭窗口
            popupwindow_calendar_bt_enter
                    .setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            dismiss();
                        }
                    });
        }
    }
}
