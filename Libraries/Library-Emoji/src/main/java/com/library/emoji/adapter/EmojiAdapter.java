package com.library.emoji.adapter;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.library.adapter_recyclerview.UniversalAdapter;
import com.library.emoji.R;
import com.library.emoji.util.SmileParserUtils;
import com.library.utils.ScreenUtils;

import java.util.List;

/**
 * @author YobertJomi
 * className EmojiAdapter
 * created at  2017/7/18  15:05
 */
public class EmojiAdapter extends UniversalAdapter<Integer> {
    private Context context;
    private OnClickedEmoji onClickedEmoji;

    public EmojiAdapter(@NonNull Context context, @LayoutRes int layoutId, @Nullable List list,
                        OnClickedEmoji
            onClickedEmoji) {
        super(context, layoutId, list);
        this.context = context;
        this.onClickedEmoji = onClickedEmoji;
    }

    @Override
    protected void getItemView(UniversalViewHolder viewHolder, final Integer info, int position) {
        ImageView imageView = viewHolder.getView(R.id.iv);
        RelativeLayout relativeRoot = viewHolder.getView(R.id.relativeRoot);
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams((ScreenUtils.getInstance()
                .getScreenWidthPx(context) - ScreenUtils.getInstance().dp2px(context, 20)) / 7,
                        RelativeLayout
                .LayoutParams.WRAP_CONTENT);
        relativeRoot.setLayoutParams(layoutParams);
        imageView.setImageResource(info);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onClickedEmoji) {
                    if (info == R.drawable.selector_delete_face) {
                        onClickedEmoji.onClickedDel();
                        return;
                    }
                    for (int i = 0; i < SmileParserUtils.resDrawable1.length; i++) {
                        if (info == SmileParserUtils.resDrawable1[i]) {
                            onClickedEmoji.onClickedEmoji(SmileParserUtils.mood1[i]);
                            return;
                        }
                    }
                    for (int i = 0; i < SmileParserUtils.resDrawable2.length; i++) {
                        if (info == SmileParserUtils.resDrawable2[i]) {
                            onClickedEmoji.onClickedEmoji(SmileParserUtils.mood2[i]);
                            return;
                        }
                    }
                }
            }
        });
    }
}
