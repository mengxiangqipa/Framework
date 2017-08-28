package com.demo.entity;


public class UpdateInfo extends Entity {

    private String version;
    private String downLoadUrl;
    private String updateContent;

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getDownLoadUrl()
    {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl)
    {
        this.downLoadUrl = downLoadUrl;
    }

    public String getUpdateContent()
    {
        return updateContent;
    }

    public void setUpdateContent(String updateContent)
    {
        this.updateContent = updateContent;
    }
}
