package com.library.emoji.fragment;

import com.library.adapter_recyclerview.UniversalAdapter;
import com.library.emoji.R;
import com.library.emoji.adapter.EmojiAdapter;
import com.library.emoji.adapter.OnClickedEmoji;
import com.library.emoji.util.SmileParserUtils;

import java.util.ArrayList;

public class Fragment_face_bb1 extends BaseAbstractFaceFragment<Integer> {
    OnClickedEmoji onClickedEmoji;

    public Fragment_face_bb1(OnClickedEmoji onClickedEmoji) {
        this.onClickedEmoji = onClickedEmoji;
    }

    @Override
    public void requestData() {
    }

    @Override
    public UniversalAdapter<Integer> initAdapter() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < 20; i++) {
            list.add(SmileParserUtils.resDrawable1[i]);
        }

        list.add(R.drawable.selector_delete_face);
        return new EmojiAdapter(getContext(), R.layout.listitem_emoji, list, onClickedEmoji);
    }
}
