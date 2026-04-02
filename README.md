# TouchSpoofer - 触摸事件欺骗模块

## 功能

让软件模拟点击看起来像真实手指触摸，绕过APP的点击检测。

## 原理

Hook `MotionEvent.getSource()` 和 `MotionEvent.getToolType()` 方法，强制返回真实触摸屏来源，让APP无法区分软件模拟点击和真实手指触摸。

## 使用方法

### 1. 安装太极(无极) 或 LSPosed

- 太极(无极)：https://taichi.cool/ （免Root）
- LSPosed：https://github.com/LSPosed/LSPosed （需要Root）

### 2. 下载并安装模块

从 [Releases](../../releases) 页面下载APK并安装。

### 3. 激活模块

- 在太极或LSPosed中找到"触摸欺骗模块"
- 勾选启用
- 勾选作用范围：大麦APP (cn.damai)

### 4. 重启大麦APP

强制停止大麦APP后重新打开。

## 编译

本项目使用GitHub Actions自动编译，Fork后即可在自己的仓库中编译APK。

## 支持的APP

目前专门针对大麦APP (cn.damai) 优化，如需支持其他APP，修改 `arrays.xml` 中的包名即可。

## 免责声明

本项目仅供学习研究使用，请勿用于违反相关法律法规的用途。
