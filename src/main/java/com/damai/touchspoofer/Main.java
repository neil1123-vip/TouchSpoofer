/**
 * Xposed模块 - 触摸事件欺骗
 *
 * 功能：Hook MotionEvent.getSource()，让所有触摸事件看起来都来自真实屏幕
 *
 * 使用方法：
 * 1. 在太极(无极)中添加此模块
 * 2. 勾选大麦APP
  * 3. 重启大麦APP
 */

package com.damai.touchspoofer;

import android.view.MotionEvent;
import android.view.InputDevice;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {

    private static final String TAG = "TouchSpoofer";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // 只对大麦APP生效
        if (!lpparam.packageName.equals("cn.damai")) {
            return;
        }

        XposedBridge.log(TAG + ": 开始Hook大麦APP");

        // Hook MotionEvent.getSource()
        // 让所有触摸事件看起来都来自真实屏幕
        XposedHelpers.findAndHookMethod(
            MotionEvent.class,
            "getSource",
            new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    // 强制返回真实触摸屏来源
                    param.setResult(InputDevice.SOURCE_TOUCHSCREEN);
                    XposedBridge.log(TAG + ": getSource() -> SOURCE_TOUCHSCREEN");
                }
            }
        );

        // Hook MotionEvent.getToolType()
        // 让触摸工具类型显示为手指
        XposedHelpers.findAndHookMethod(
            MotionEvent.class,
            "getToolType",
            int.class,
            new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    // TOOL_TYPE_FINGER = 1
                    param.setResult(1);
                    XposedBridge.log(TAG + ": getToolType() -> FINGER");
                }
            }
        );

        XposedBridge.log(TAG + ": Hook完成");
    }
}
