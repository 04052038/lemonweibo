LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_SRC_FILES := com_star_yytv_JniInterface.c
LOCAL_C_INCLUDES := $(JNI_H_INCLUDE)
LOCAL_MODULE := libyyserver
# LOCAL_LDLIBS += -llog
# LOCAL_SHARED_LIBRARIES := libutils
LOCAL_PRELINK_MODULE := false
include $(BUILD_SHARED_LIBRARY)