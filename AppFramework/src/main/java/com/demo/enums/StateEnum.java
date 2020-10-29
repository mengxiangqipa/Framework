package com.demo.enums;

/**
 * 通用状态
 *
 * @author YobertJomi
 * className StateEnum
 * created at  2020/3/10  21:06
 */
public enum StateEnum {
    /**
     * 无状态
     */
    NONE(0),
    /**
     * 执行中
     */
    DOING(1),
    /**
     * 失败
     */
    FAIL(2),
    /**
     * 成功
     */
    SUCCESS(3);

    private int state;

    StateEnum(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}

