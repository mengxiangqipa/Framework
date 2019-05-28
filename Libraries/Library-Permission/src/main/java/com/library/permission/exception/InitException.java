package com.library.permission.exception;


public class InitException extends IllegalStateException {

    public InitException() {
        super("auto init failed ,you need invoke SoulPermission.init() in your application");
    }
}
