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
 */
public class MainHook implements IXposedHookLoadPackage {

    private static final String TAG = "TouchSpoofer";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // 只对大麦APP生效
        if (!lpparam.packageName.equals("cn.damai")) {
            return;
        }

        log("开始Hook大麦APP");

        // 方案1: 尝试Hook MotionEvent (可能失败)
        hookMotionEvent(lpparam);

        log("Hook初始化完成");
    }

    private void hookMotionEvent(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            // 使用反射方式Hook，更安全
            Class<?> motionEventClass = MotionEvent.class;

            XposedHelpers.findAndHookMethod(
                motionEventClass,
                "getSource",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        try {
                            // SOURCE_TOUCHSCREEN = 0x00001000 = 4096
                            param.setResult(0x00001000);
                        } catch (Throwable t) {
                            // 忽略错误，保持原值
                        }
                    }
                }
            );
            log("Hook getSource() 成功");
        } catch (Throwable t) {
            log("Hook getSource() 失败: " + t.getMessage());
        }

        try {
            XposedHelpers.findAndHookMethod(
                MotionEvent.class,
                "getToolType",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        try {
                            // TOOL_TYPE_FINGER = 1
                            param.setResult(1);
                        } catch (Throwable t) {
                            // 忽略错误
                        }
                    }
                }
            );
            log("Hook getToolType() 成功");
        } catch (Throwable t) {
            log("Hook getToolType() 失败: " + t.getMessage());
        }
    }

    private void log(String msg) {
        try {
            XposedBridge.log(TAG + ": " + msg);
        } catch (Throwable t) {
            // 忽略
        }
    }
}
