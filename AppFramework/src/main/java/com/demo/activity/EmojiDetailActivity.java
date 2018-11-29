package com.demo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.demo.R;
import com.framework.utils.ScreenUtils;
import com.framework.utils.ToastUtil;
import com.library.adapter_listview.CommonPagerAdapter;
import com.library.emoji.adapter.OnClickedEmoji;
import com.library.emoji.fragment.Fragment_face_bb1;
import com.library.emoji.fragment.Fragment_face_bb2;
import com.library.emoji.fragment.Fragment_face_bb3;
import com.library.emoji.fragment.Fragment_face_bb4;
import com.library.emoji.fragment.Fragment_face_ee1;
import com.library.emoji.fragment.Fragment_face_ee2;
import com.library.emoji.fragment.Fragment_face_ee3;
import com.library.emoji.fragment.Fragment_face_ee4;
import com.library.emoji.util.SmileParserUtils;
import com.library.percent.PercentLinearLayout;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import custom.org.greenrobot.eventbus.Subscribe;
import custom.org.greenrobot.eventbus.ThreadMode;

/**
 * emoji详情
 *
 * @author YobertJomi
 * className EmojiDetailActivity
 * created at  2017/8/21  13:50
 */
public class EmojiDetailActivity extends BaseActivity {

    @BindView(R.id.ivPic)
    ImageView ivPic;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.bottomComment)
    PercentLinearLayout bottomComment;
    @BindView(R.id.emojiViewPager)
    ViewPager emojiViewPager;
    @BindView(R.id.dotsLayout)
    LinearLayout dotsLayout;
    @BindView(R.id.ivBBexpression)
    ImageView ivBBexpression;
    @BindView(R.id.emojiOther)
    RelativeLayout emojiOther;
    @BindView(R.id.ivDefault)
    ImageView ivDefault;
    @BindView(R.id.emojiDefault)
    RelativeLayout emojiDefault;

    private CommonPagerAdapter emojiAdapter;
    private Fragment fragment_1;
    private Fragment fragment_2;
    private Fragment fragment_3;
    private Fragment fragment_4;
    private Fragment fragment_5;
    private Fragment fragment_6;
    private Fragment fragment_7;
    private Fragment fragment_8;
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < dotsLayout.getChildCount(); i++) {
                dotsLayout.getChildAt(i).setBackgroundResource(i == position % dotsLayout.getChildCount() ? R
                        .drawable.shape_dot3 : R.drawable.shape_dot1);
            }
            emojiOther.setBackgroundResource(position >= 4 ? R.color.spaceline : R.color.white);
            emojiDefault.setBackgroundResource(position >= 4 ? R.color.white : R.color.spaceline);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private OnClickedEmoji onClickedEmoji = new OnClickedEmoji() {
        @Override
        public void onClickedEmoji(String emoji) {
            etComment.setText(SmileParserUtils.getInstance().getSmiledText(EmojiDetailActivity.this, etComment
                    .getText() + emoji, 22, 22));
            etComment.setSelection(etComment.getText().length());
        }

        @Override
        public void onClickedDel() {
            int action = KeyEvent.ACTION_DOWN;
            int code = KeyEvent.KEYCODE_DEL;
            KeyEvent event = new KeyEvent(action, code);
            etComment.onKeyDown(KeyEvent.KEYCODE_DEL, event);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji_detail);
        ButterKnife.bind(this);
        ScreenUtils.getInstance().setTranslucentStatus(this, true);
        ScreenUtils.getInstance().setStatusBarTintColor(this,
                getResources().getColor(R.color.white));
    }

    //eventBus通知新消息
    @Subscribe(threadMode = ThreadMode.MAIN, tag = "newMessage")
    public void receivedNewMessage(String info) {
    }

    @OnClick({R.id.bottomComment, R.id.emojiOther, R.id.emojiDefault, R.id.ivPic, R.id.tvSend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.emojiOther:
                emojiOther.setBackgroundResource(R.color.white);
                emojiDefault.setBackgroundResource(R.color.spaceline);
                emojiViewPager.setCurrentItem(0);
                break;
            case R.id.emojiDefault:
                emojiOther.setBackgroundResource(R.color.spaceline);
                emojiDefault.setBackgroundResource(R.color.white);
                emojiViewPager.setCurrentItem(4);
                break;
            case R.id.ivPic:
                bottomComment.setVisibility(View.VISIBLE);
                if (emojiAdapter == null) {
                    fragment_1 = new Fragment_face_bb1(onClickedEmoji);
                    fragment_2 = new Fragment_face_bb2(onClickedEmoji);
                    fragment_3 = new Fragment_face_bb3(onClickedEmoji);
                    fragment_4 = new Fragment_face_bb4(onClickedEmoji);
                    fragment_5 = new Fragment_face_ee1(onClickedEmoji);
                    fragment_6 = new Fragment_face_ee2(onClickedEmoji);
                    fragment_7 = new Fragment_face_ee3(onClickedEmoji);
                    fragment_8 = new Fragment_face_ee4(onClickedEmoji);
                    final List<Fragment> list = new ArrayList<>();
                    String[] titles = new String[]{"Mobile", "PHOTO", "FIND PROS"};
                    list.add(fragment_1);
                    list.add(fragment_2);
                    list.add(fragment_3);
                    list.add(fragment_4);
                    list.add(fragment_5);
                    list.add(fragment_6);
                    list.add(fragment_7);
                    list.add(fragment_8);
                    emojiAdapter = new CommonPagerAdapter(EmojiDetailActivity.this, getSupportFragmentManager(),
                            list, titles);
                    emojiViewPager.setAdapter(emojiAdapter);
                    emojiViewPager.addOnPageChangeListener(onPageChangeListener);
                }
                addDots(4);
                break;
            case R.id.tvSend:
                ToastUtil.getInstance().showToast("发送,,,,...");
                break;
        }
    }

    /**
     * 添加小圆点
     */
    private void addDots(int lenth) {
        if (dotsLayout.getChildCount() <= 0)
            //加小圆点
            for (int i = 0; i < lenth; i++) {
                TextView textView;
                int margin = ScreenUtils.getInstance().dip2px(this, 5);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getInstance().dip2px
                        (this, 8), ScreenUtils.getInstance().dip2px(this, 8));
                if (i == 0) {
                    textView = new TextView(this);
                    textView.setBackgroundResource(R.drawable.shape_dot3);
                    params.leftMargin = 0;
                    params.rightMargin = margin;
                } else if (i == lenth - 1) {
                    textView = new TextView(this);
                    textView.setBackgroundResource(R.drawable.shape_dot1);
                    params.leftMargin = margin;
                    params.rightMargin = 0;
                } else {
                    textView = new TextView(this);
                    textView.setBackgroundResource(R.drawable.shape_dot1);
                    params.leftMargin = margin;
                    params.rightMargin = margin;
                }
                textView.setLayoutParams(params);
                dotsLayout.addView(textView);
            }
    }
}