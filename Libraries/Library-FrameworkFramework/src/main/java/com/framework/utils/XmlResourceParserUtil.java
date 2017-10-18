package com.framework.utils;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.framework.R;
import com.framework.entity.IpInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * XmlResourceParser 解析工具
 *
 * @author YobertJomi
 *         className XmlResourceParserUtil
 *         created at  2017/8/17  18:05
 */
public class XmlResourceParserUtil
{

    private static volatile XmlResourceParserUtil singleton;

    private XmlResourceParserUtil()
    {
    }

    public static XmlResourceParserUtil getInstance()
    {
        if (singleton == null)
        {
            synchronized (XmlResourceParserUtil.class)
            {
                if (singleton == null)
                {
                    singleton = new XmlResourceParserUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * 释放静态对象
     */
    public void releaseInstance()
    {
        if (singleton != null)
            singleton = null;
    }

    public List<IpInfo> parseXML(Context context)
    {
        List<IpInfo> list = new ArrayList<IpInfo>();
        if (context == null)
            return null;
        IpInfo ipInfo = null;
        IpInfo ipInfoChild = null;
        XmlResourceParser xmlResourceParser = context.getResources().getXml(R.xml.ipconfig);
        try
        {
            while (xmlResourceParser.getEventType() != XmlResourceParser.END_DOCUMENT)
            {
                if (xmlResourceParser.getEventType() == XmlResourceParser.START_TAG)
                {
                    String tagName = xmlResourceParser.getName();
                    if (tagName.equals("city"))
                    {
                        String cityName = xmlResourceParser.getAttributeValue(0);
                        ipInfo = new IpInfo();
                        ipInfo.setCity(cityName);
                    } else if (tagName.equals("shop"))
                    {
                        String shop = xmlResourceParser.getAttributeValue(0);
                        ipInfoChild = new IpInfo();
                        ipInfoChild.setShop(shop);
                    } else if (tagName.equals("ip"))
                    {
                        String ip = xmlResourceParser.getAttributeValue(0);
                        if (null != ipInfoChild && null != ipInfo)
                        {
                            ipInfoChild.setIp(ip);
                            ipInfoChild.setCity(ipInfo.getCity());
                            ipInfo.getArrayList().add(ipInfoChild);
                        }
                    }

                } else if (xmlResourceParser.getEventType() == XmlResourceParser.END_TAG)
                {
                    String tagName = xmlResourceParser.getName();
                    if (tagName.equals("city"))
                    {
                        if (ipInfo != null)
                        {
                            list.add(ipInfo);
                        }
                    }

                }
                xmlResourceParser.next();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

}
