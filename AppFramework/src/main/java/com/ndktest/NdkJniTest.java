package com.ndktest;

/**
 * NDKtest
 *     @author Yangjie
 *     className NdkJniTest
 *     created at  2017/2/17  10:11
 */
public class NdkJniTest {
    static {
        System.loadLibrary("ndktest");   //defaultConfig.ndk.moduleName
    }
    public native String myNDK();

}
