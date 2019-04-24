package com.framework2.netty;

import java.io.Serializable;

/**
 * Netty 消息实体
 *
 * @author YobertJomi
 * className NettyInfo
 * created at  2017/9/11  16:06
 */
public class NettyInfo implements Serializable {
    private String msg;
    private TYPE type;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public enum TYPE {
        CHANNEL_ACTIVE,
        CHANNEL_INACTIVE,
        MESSAGE_RECEIVED,
        CHANNEL_READCOMPLETE,
        EXCEPTION_CAUGHT
    }
}
