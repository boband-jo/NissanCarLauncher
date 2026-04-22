
# 日产车机桌面 - NissanCarLauncher

专为日产Android 4.4车机系统设计的智能桌面应用，完美融合嘟嘟桌面的设计理念与车机特定功能。

## 功能特性

### 1. 红绿灯提醒
- 悬浮窗显示红绿灯状态
- 倒计时显示
- 自动切换红/黄/绿状态

### 2. 车机数据读取
- 实时车速显示
- 油耗/油量监控
- 车内温度显示
- GPS位置定位
- 可扩展的数据接口

### 3. 方控映射
- 支持媒体按键（播放/暂停/上一曲/下一曲）
- 音量控制
- 电话按键支持
- 可自定义映射规则

### 4. 蓝牙歌词显示
- 蓝牙音乐连接检测
- 悬浮歌词显示
- 歌词滚动动画
- 自动显示/隐藏

### 5. 智能桌面
- 常用应用快速启动
- 支持CarLife、导航、音乐等
- 时间日期显示
- 车机状态信息栏
- 深色主题设计

## 系统要求

- Android 4.4 (API 19) 或更高版本
- 日产车机系统 (NTDA3)
- 支持悬浮窗权限

## 构建项目

### 前置条件

- Android SDK (API 19)
- Gradle 2.x
- JDK 7 或更高

### 构建步骤

1. 克隆或下载项目
2. 使用 Android Studio 打开项目，或使用命令行：

```bash
cd NissanCarLauncher
./gradlew assembleDebug
```

3. APK 文件将生成在 `app/build/outputs/apk/` 目录

### 直接安装

如果您有现有的APK，直接使用adb安装：

```bash
adb install NissanCarLauncher.apk
```

## 配置说明

### 权限配置

应用需要以下权限：

- INTERNET - 网络访问
- ACCESS_FINE_LOCATION - GPS定位
- BLUETOOTH - 蓝牙连接
- SYSTEM_ALERT_WINDOW - 悬浮窗
- RECEIVE_BOOT_COMPLETED - 开机自启
- READ_EXTERNAL_STORAGE - 存储访问

### 应用列表配置

在 `MainActivity.java` 的 `initAppList()` 方法中可以自定义应用列表：

```java
addApp("应用名称", "包名", "Activity名称", R.drawable.icon);
```

## 项目结构

```
NissanCarLauncher/
├── app/
│   ├── src/main/
│   │   ├── java/com/nissan/carlauncher/
│   │   │   ├── MainActivity.java          # 主界面
│   │   │   ├── BootReceiver.java          # 开机自启
│   │   │   ├── MediaButtonReceiver.java   # 方控接收
│   │   │   ├── BluetoothLyricsService.java # 蓝牙歌词服务
│   │   │   ├── CarDataService.java        # 车机数据服务
│   │   │   └── TrafficLightService.java   # 红绿灯服务
│   │   ├── res/
│   │   │   ├── layout/                    # 布局文件
│   │   │   ├── drawable/                  # 图标资源
│   │   │   └── values/                    # 字符串/颜色/样式
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

## 自定义开发

### 添加新的数据类型

在 `CarDataService.java` 中添加新的数据监听器和模拟数据。

### 自定义方控映射

修改 `MediaButtonReceiver.java` 中的 `handleMediaKey()` 方法。

### 添加新的应用快捷方式

在 `MainActivity.java` 的 `initAppList()` 方法中添加新应用。

## 注意事项

1. **Android 4.4兼容性**：本应用专门为Android 4.4设计，使用的API都是该版本支持的
2. **悬浮窗权限**：首次使用需要授予悬浮窗权限
3. **GPS权限**：需要位置权限才能获取车速和位置信息
4. **蓝牙权限**：需要蓝牙权限才能显示歌词

## 与日产车机的集成

本应用针对日产NTDA3车机系统进行了优化：
- 使用系统级权限
- 与原车系统兼容
- 支持原车方控协议
- 适配车机屏幕分辨率

## 许可证

本项目仅供学习和研究使用。

## 致谢

- 参考了嘟嘟桌面的设计理念
- 基于CarLife的车机集成方案
- 感谢日产车机社区的支持
