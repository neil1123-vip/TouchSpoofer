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
 * Hook所有可能被检测的触摸相关方法
 */
public class MainHook implements IXposedHookLoadPackage {

    private static final String TAG = "TouchSpoofer";
    private static final int SOURCE_TOUCHSCREEN = 0x00001000;
    private static final int TOOL_TYPE_FINGER = 1;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("cn.damai")) {
            return;
        }

        log("开始Hook大麦APP");
        showToast(lpparam, "TouchSpoofer模块已加载");

        // Hook MotionEvent相关方法
        hookMotionEvent(lpparam);

        // Hook InputDevice相关方法
        hookInputDevice(lpparam);

        log("Hook初始化完成");
    }

    private void hookMotionEvent(XC_LoadPackage.LoadPackageParam lpparam) {
        // Hook getSource()
        try {
            XposedHelpers.findAndHookMethod(MotionEvent.class, "getSource",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(SOURCE_TOUCHSCREEN);
                    }
                });
            log("Hook getSource() 成功");
        } catch (Throwable t) {
            log("Hook getSource() 失败: " + t.getMessage());
        }

        // Hook getToolType()
        try {
            XposedHelpers.findAndHookMethod(MotionEvent.class, "getToolType", int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(TOOL_TYPE_FINGER);
                    }
                });
            log("Hook getToolType() 成功");
        } catch (Throwable t) {
            log("Hook getToolType() 失败: " + t.getMessage());
        }

        // Hook getDeviceId() - 返回触摸屏设备ID
        try {
            XposedHelpers.findAndHookMethod(MotionEvent.class, "getDeviceId",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(0); // 真实触摸屏通常是0或正数
                    }
                });
            log("Hook getDeviceId() 成功");
        } catch (Throwable t) {
            log("Hook getDeviceId() 失败: " + t.getMessage());
        }

        // Hook getPressure() - 返回正常压力值
        try {
            XposedHelpers.findAndHookMethod(MotionEvent.class, "getPressure", int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(0.5f); // 正常压力值范围
                    }
                });
            log("Hook getPressure() 成功");
        } catch (Throwable t) {
            log("Hook getPressure() 失败: " + t.getMessage());
        }

        // Hook getSize() - 返回正常触点大小
        try {
            XposedHelpers.findAndHookMethod(MotionEvent.class, "getSize", int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(0.1f); // 正常触点大小
                    }
                });
            log("Hook getSize() 成功");
        } catch (Throwable t) {
            log("Hook getSize() 失败: " + t.getMessage());
        }

        // Hook getToolMajor() - 返回正常工具尺寸
        try {
            XposedHelpers.findAndHookMethod(MotionEvent.class, "getToolMajor", int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(10.0f);
                    }
                });
            log("Hook getToolMajor() 成功");
        } catch (Throwable t) {
            log("Hook getToolMajor() 失败: " + t.getMessage());
        }

        // Hook getToolMinor()
        try {
            XposedHelpers.findAndHookMethod(MotionEvent.class, "getToolMinor", int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(10.0f);
                    }
                });
            log("Hook getToolMinor() 成功");
        } catch (Throwable t) {
            log("Hook getToolMinor() 失败: " + t.getMessage());
        }

        showToast(lpparam, "MotionEvent Hook完成");
    }

    private void hookInputDevice(XC_LoadPackage.LoadPackageParam lpparam) {
        // Hook InputDevice.getSource()
        try {
            XposedHelpers.findAndHookMethod(InputDevice.class, "getSource",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(SOURCE_TOUCHSCREEN);
                    }
                });
            log("Hook InputDevice.getSource() 成功");
        } catch (Throwable t) {
            log("Hook InputDevice.getSource() 失败: " + t.getMessage());
        }

        // Hook InputDevice.getName()
        try {
            XposedHelpers.findAndHookMethod(InputDevice.class, "getName",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult("touchscreen");
                    }
                });
            log("Hook InputDevice.getName() 成功");
        } catch (Throwable t) {
            log("Hook InputDevice.getName() 失败: " + t.getMessage());
        }

        showToast(lpparam, "InputDevice Hook完成");
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
                    }
                }
            });
        } catch (Throwable t) {
        }
    }

    private void log(String msg) {
        try {
            XposedBridge.log(TAG + ": " + msg);
        } catch (Throwable t) {
        }
    }
}
