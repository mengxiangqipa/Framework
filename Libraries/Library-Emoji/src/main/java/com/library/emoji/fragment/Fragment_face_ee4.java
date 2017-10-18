package com.library.emoji.fragment;


import com.library.adapter_recyclerview.UniversalAdapter;
import com.library.emoji.R;
import com.library.emoji.adapter.EmojiAdapter;
import com.library.emoji.adapter.OnClickedEmoji;
import com.library.emoji.util.SmileParserUtils;

import java.util.ArrayList;

public class Fragment_face_ee4 extends BaseAbstractFaceFragment<Integer>
{
    public Fragment_face_ee4(OnClickedEmoji onClickedEmoji)
    {
        this.onClickedEmoji = onClickedEmoji;
    }

    @Override
    public void requestData()
    {

    }

    @Override
    public UniversalAdapter<Integer> initAdapter()
    {
        ArrayList list = new ArrayList();
        for (int i = 0; i < 12; i++)
        {
            list.add(SmileParserUtils.resDrawable2[60 + i]);
        }
        list.add(R.drawable.selector_delete_face);

        return new EmojiAdapter(getContext(), R.layout.listitem_emoji, list, onClickedEmoji)
        {
            @Override
            public int resetSpanSizeLookup(int realPosition, int spanCount, int getItemViewType)
            {
                if (realPosition == 15)
                    return 6;
                return super.resetSpanSizeLookup(realPosition, spanCount, getItemViewType);
            }
        };
    }

    private OnClickedEmoji onClickedEmoji;
}
