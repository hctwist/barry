LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := keystore
LOCAL_SRC_FILES := keystore.c

include $(BUILD_SHARED_LIBRARY)