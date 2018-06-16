package com.framework.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * IP配置的相关信息
 *
 * @author Yangjie
 * className IpInfo
 * created at  2016/11/11  15:11
 */
public class IpInfo implements Parcelable {
    public static final Creator<IpInfo> CREATOR = new Creator<IpInfo>() {
        @Override
        public IpInfo createFromParcel(Parcel in) {
            return new IpInfo(in);
        }

        @Override
        public IpInfo[] newArray(int size) {
            return new IpInfo[size];
        }
    };
    private String city;
    private String ip;
    private String shop;//店名
    private ArrayList<IpInfo> arrayList = new ArrayList<IpInfo>();

    public IpInfo() {
    }

    protected IpInfo(Parcel in) {
        city = in.readString();
        ip = in.readString();
        shop = in.readString();
        arrayList = in.createTypedArrayList(IpInfo.CREATOR);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public ArrayList<IpInfo> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<IpInfo> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(ip);
        dest.writeString(shop);
        dest.writeTypedList(arrayList);
    }
}
