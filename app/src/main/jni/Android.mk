LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

APP_BUILD_SCRIPT := $(LOCAL_PATH)/foo.mk

LOCAL_MODULE    := keystore
LOCAL_SRC_FILES := keystore.c

include $(BUILD_SHARED_LIBRARY)