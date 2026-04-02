package com.damai.touchspoofer;

import android.view.MotionEvent;
import android.view.InputDevice;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Xposed模块 - 触摸事件欺骗
 *
 * 功能：Hook MotionEvent.getSource()，让所有触摸事件看起来都来自真实屏幕
 *
 * 使用方法：
 * 1. 在太极(无极)或LSPosed中添加此模块
 * 2. 勾选大麦APP
 * 3. 重启大麦APP
 */
public class MainHook implements IXposedHookLoadPackage {

    private static final String TAG = "TouchSpoofer";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // 只对大麦APP生效
        if (!lpparam.packageName.equals("cn.damai")) {
            return;
        }

        XposedBridge.log(TAG + ": 开始Hook大麦APP");

        try {
            // Hook MotionEvent.getSource()
            // 让所有触摸事件看起来都来自真实触摸屏
            XposedHelpers.findAndHookMethod(
                MotionEvent.class,
                "getSource",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        // 强制返回真实触摸屏来源
                        // SOURCE_TOUCHSCREEN = 0x00001000
                        param.setResult(InputDevice.SOURCE_TOUCHSCREEN);
                    }
                }
            );

            XposedBridge.log(TAG + ": Hook getSource() 成功");
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": Hook getSource() 失败: " + t.getMessage());
        }

        try {
            // Hook MotionEvent.getToolType(int pointerIndex)
            // 让触摸工具类型显示为手指
            XposedHelpers.findAndHookMethod(
                MotionEvent.class,
                "getToolType",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        // TOOL_TYPE_FINGER = 1
                        param.setResult(MotionEvent.TOOL_TYPE_FINGER);
                    }
                }
            );

            XposedBridge.log(TAG + ": Hook getToolType() 成功");
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": Hook getToolType() 失败: " + t.getMessage());
        }

        XposedBridge.log(TAG + ": Hook完成");
    }
}
