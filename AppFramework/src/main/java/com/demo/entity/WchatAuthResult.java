package com.demo.entity;


/**
 * wchat 授权返回
 *
 * @author YobertJomi
 *         className WchatAuthResult
 *         created at  2017/4/25  16:26
 */
public class WchatAuthResult extends Entity {

    private String authCode;
    private boolean success;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
