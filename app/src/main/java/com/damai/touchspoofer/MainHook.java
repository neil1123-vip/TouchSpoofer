package com.damai.touchspoofer;

import android.view.MotionEvent;
import android.view.InputDevice;
import android.widget.Toast;
import android.os.Handler;
import android.os.Looper;

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
        showToast(lpparam, "TouchSpoofer模块已加载");

        // Hook MotionEvent
        hookMotionEvent(lpparam);

        log("Hook初始化完成");
    }

    private void hookMotionEvent(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            XposedHelpers.findAndHookMethod(
                MotionEvent.class,
                "getSource",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        try {
                            param.setResult(0x00001000);
                        } catch (Throwable t) {
                        }
                    }
                }
            );
            log("Hook getSource() 成功");
            showToast(lpparam, "Hook getSource() 成功");
        } catch (Throwable t) {
            log("Hook getSource() 失败: " + t.getMessage());
            showToast(lpparam, "Hook getSource() 失败: " + t.getMessage());
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
                            param.setResult(1);
                        } catch (Throwable t) {
                        }
                    }
                }
            );
            log("Hook getToolType() 成功");
            showToast(lpparam, "Hook getToolType() 成功");
        } catch (Throwable t) {
            log("Hook getToolType() 失败: " + t.getMessage());
            showToast(lpparam, "Hook getToolType() 失败: " + t.getMessage());
        }
    }

    private void showToast(final XC_LoadPackage.LoadPackageParam lpparam, final String msg) {
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Toast.makeText(
                            (android.content.Context) lpparam.classLoader.loadClass("android.app.ActivityThread")
                                .getMethod("currentApplication")
                                .invoke(null),
                            "TouchSpoofer: " + msg,
                            Toast.LENGTH_SHORT
                        ).show();
                    } catch (Throwable t) {
                        log("Toast失败: " + t.getMessage());
                    }
                }
            });
        } catch (Throwable t) {
            log("showToast失败: " + t.getMessage());
        }
    }

    private void log(String msg) {
        try {
            XposedBridge.log(TAG + ": " + msg);
        } catch (Throwable t) {
        }
    }
}
