LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := ndktest
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	D:\AndroidStudio_project_test\Demo-AppFramework\app\src\main\jni\com_ndktest_NdkJniTest.c \

LOCAL_C_INCLUDES += D:\AndroidStudio_project_test\Demo-AppFramework\app\src\main\jni
LOCAL_C_INCLUDES += D:\AndroidStudio_project_test\Demo-AppFramework\app\src\company\jni
LOCAL_C_INCLUDES += D:\AndroidStudio_project_test\Demo-AppFramework\app\src\debug\jni
LOCAL_C_INCLUDES += D:\AndroidStudio_project_test\Demo-AppFramework\app\src\companyDebug\jni

include $(BUILD_SHARED_LIBRARY)
#该文件来至build/intermediates/ndk
