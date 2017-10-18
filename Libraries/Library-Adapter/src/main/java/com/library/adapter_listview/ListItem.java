package com.library.adapter_listview;

import java.util.ArrayList;
import java.util.List;

public class ListItem<T> extends Entity {
    public String pi = "";
    public String flag = "";
    public String msg = "";
    public List mdatas = new ArrayList<T>();
    public boolean isDataNULL = false;
    public List arrayList1 = new ArrayList<ListItem>();
}
