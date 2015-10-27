LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := andengine_shared
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	C:\Workspace\GameDev\AndEngine\AndEngineTest\andEngine\src\main\jni\Android.mk \
	C:\Workspace\GameDev\AndEngine\AndEngineTest\andEngine\src\main\jni\Application.mk \
	C:\Workspace\GameDev\AndEngine\AndEngineTest\andEngine\src\main\jni\build.sh \
	C:\Workspace\GameDev\AndEngine\AndEngineTest\andEngine\src\main\jni\src\BufferUtils.cpp \
	C:\Workspace\GameDev\AndEngine\AndEngineTest\andEngine\src\main\jni\src\GLES20Fix.c \

LOCAL_C_INCLUDES += C:\Workspace\GameDev\AndEngine\AndEngineTest\andEngine\src\main\jni
LOCAL_C_INCLUDES += C:\Workspace\GameDev\AndEngine\AndEngineTest\andEngine\src\release\jni

include $(BUILD_SHARED_LIBRARY)
