package com.library.permission.exception;

public class ContainerStatusException extends IllegalStateException {

    public ContainerStatusException() {
        super(" activity did not existence, check your app status before use Library-Permission");
    }
}
