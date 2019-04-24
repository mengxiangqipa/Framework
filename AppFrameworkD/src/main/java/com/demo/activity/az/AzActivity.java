package com.demo.activity.az;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

import com.demo.demo.R;
import com.library.adapter_recyclerview.SectionItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AzActivity extends AppCompatActivity {

    SectionAdapter adapter;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.etSearch)
    EditTextWithDel etSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvCircle)
    TextView tvCircle;
    @BindView(R.id.sideBar)
    SideBar sideBar;
    private List<ContactSortModel> SourceDateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.az_activity);
        ButterKnife.bind(this);
        initDatas();
        initEvents();
        setAdapter();
    }

    private void setAdapter() {
        SourceDateList = filledData(getResources().getStringArray(R.array.contacts));

        Collections.sort(SourceDateList, new PinyinComparator());
        adapter = new SectionAdapter(this, R.layout.az_listitem_contact, SourceDateList);
        recyclerView.addItemDecoration(new SectionItemDecoration(new SectionItemDecoration.SectionCallBack() {
            //返回标记id (即每一项对应的标志性的字符串)
            @Override
            public String getSectionId(int position) {
                try {
                    return SourceDateList.get(position).getSortLetters();
                } catch (Exception e) {
                    return "";
                }
            }

            //获取同组中的第一个内容
            @Override
            public String getSectionTitle(int position) {
                try {
                    return SourceDateList.get(position).getSortLetters();
                } catch (Exception e) {
                    return "";
                }
            }
        }).setTextSize(40f).setBackgroudColor(AzActivity.this, R.color.sectionColor).setTextLeftMargin(40)
                .setSectionHeight(100)
                .setTextColor(AzActivity.this, R.color.colorAccent));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initEvents() {
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = getPositionForSection(s.charAt(0));
                if (position != -1) {
//                    recyclerView.smoothScrollToPosition(position);
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 0);
                }
            }
        });

        //ListView的点击事件
//        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                tvTitle.setText(((ContactSortModel) adapter.getItem(position - 1)).getName());
//                Toast.makeText(getApplication(), ((ContactSortModel) adapter.getItem(position)).getName(), Toast
// .LENGTH_SHORT).show();
//            }
//        });

        //根据输入框输入值的改变来过滤搜索
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initDatas() {
        sideBar.setTextView(tvCircle);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < SourceDateList.size(); i++) {
            String sortStr = SourceDateList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    private void filterData(String filterStr) {
        List<ContactSortModel> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = SourceDateList;
        } else {
            mSortList.clear();
            for (ContactSortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || PinyinUtils.getPingYin
                        (name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparator());
        adapter.updateData(mSortList);
    }

    private List<ContactSortModel> filledData(String[] date) {
        List<ContactSortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexList = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            ContactSortModel sortModel = new ContactSortModel();
            sortModel.setName(date[i]);
            String pinyin = PinyinUtils.getPingYin(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
                if (!indexList.contains(sortString)) {
                    indexList.add(sortString);
                }
            } else {
                sortModel.setSortLetters("#");
                if (!indexList.contains("#")) {
                    indexList.add(0, "#");
                }
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexList);
        sideBar.setIndexText(indexList);//过滤右侧无用的index
        return mSortList;
    }
}
