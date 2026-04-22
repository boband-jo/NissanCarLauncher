
# 日产车机桌面 - 构建指南

## 方案一：使用 Android Studio (推荐)

### 步骤

1. **安装 Android Studio**
   - 下载并安装 Android Studio (建议使用较旧版本以兼容 API 19)
   - Android Studio 2.x 或 3.x 版本最佳

2. **配置 SDK**
   - 打开 SDK Manager
   - 安装 Android 4.4 (API 19) SDK Platform
   - 安装 Android SDK Build-Tools 28.0.3
   - 安装 Android Support Repository

3. **导入项目**
   - 启动 Android Studio
   - 选择 "Open an existing Android Studio project"
   - 选择 NissanCarLauncher 文件夹
   - 等待 Gradle 同步完成

4. **构建 APK**
   - 菜单: Build → Build Bundle(s) / APK(s) → Build APK(s)
   - 或点击工具栏的 "Make Project" 按钮
   - APK 位置: `app/build/outputs/apk/debug/app-debug.apk`

## 方案二：使用命令行构建

### 前置条件

- JDK 7 或更高版本 (建议 JDK 8)
- Android SDK (API 19)
- 设置环境变量:
  - `ANDROID_HOME` 指向 Android SDK 目录
  - `JAVA_HOME` 指向 JDK 目录
  - 将 `%ANDROID_HOME%\platform-tools` 和 `%ANDROID_HOME%\tools` 添加到 PATH

### Windows 构建步骤

```batch
cd NissanCarLauncher

# 1. 使用 Gradle Wrapper 构建
gradlew.bat assembleDebug

# 2. 或者直接使用已安装的 Gradle
gradle assembleDebug
```

### Linux/Mac 构建步骤

```bash
cd NissanCarLauncher

# 使用 Gradle Wrapper
chmod +x gradlew
./gradlew assembleDebug
```

### 输出文件

构建成功后，APK 文件位于:
```
app/build/outputs/apk/debug/app-debug.apk
```

## 方案三：使用 apktool 重新打包 (简单方式)

如果您不想配置完整的 Android 开发环境，可以使用以下方式：

### 步骤

1. **确保已安装 Java**
   - 下载并安装 JDK 8 或更高版本

2. **使用现有的 APK 作为基础**
   - 复制一个简单的 Android APK 作为模板
   - 或者使用 CarLife 等车机应用作为基础

3. **解压并修改**
   ```batch
   cd d:\车机应用
   java -jar apktool.jar d template.apk -o temp_apk
   ```

4. **替换文件**
   - 将我们的代码和资源文件复制到解压后的目录
   - 替换 AndroidManifest.xml, 资源文件等

5. **重新打包**
   ```batch
   java -jar apktool.jar b temp_apk -o NissanCarLauncher.apk
   ```

6. **签名 APK** (重要)
   ```batch
   # 使用 debug 密钥签名
   # 或者使用您自己的密钥
   ```

## 方案四：使用在线构建服务

如果您没有本地环境，可以使用在线构建服务：

1. 将项目上传到 GitHub
2. 使用 GitHub Actions, CircleCI 或类似服务
3. 配置构建脚本

## 安装到车机

### 使用 ADB 安装

```bash
# 连接到车机
adb connect &lt;车机IP&gt;:5555

# 安装 APK
adb install -r app/build/outputs/apk/debug/app-debug.apk

# 如果需要，设置为默认桌面
adb shell pm set-home-activity com.nissan.carlauncher/.MainActivity
```

### U盘安装

1. 将 APK 复制到 U盘
2. 插入车机
3. 使用文件管理器安装

## 常见问题

### 问题：Gradle 同步失败

**解决方案：**
- 检查网络连接
- 配置 Gradle 镜像源
- 在项目根目录的 `build.gradle` 中添加:

```gradle
allprojects {
    repositories {
        maven { url 'https://maven.aliyun.com/repository/google' }
        maven { url 'https://maven.aliyun.com/repository/jcenter' }
    }
}
```

### 问题：找不到 SDK

**解决方案：**
- 在 `local.properties` 文件中设置:
```
sdk.dir=C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk
```

### 问题：编译错误

**解决方案：**
- 确保使用正确的 Build Tools 版本
- 检查 API 级别是否兼容
- 清理项目并重新构建:
```bash
gradlew clean
gradlew assembleDebug
```

### 问题：车机无法安装

**解决方案：**
- 检查 APK 是否正确签名
- 确保 APK 支持 armeabi-v7a 架构
- 检查车机的 Android 版本

## 调试技巧

### 查看日志

```bash
adb logcat | grep NissanCarLauncher
```

### 调试应用

```bash
# 启动应用
adb shell am start -n com.nissan.carlauncher/.MainActivity

# 查看当前 Activity
adb shell dumpsys activity top
```

## 快速开始模板

如果您想快速测试，我提供了以下替代方案：

1. **使用现有车机应用修改**
   - 解压 CarLife 或其他车机应用
   - 修改资源和代码
   - 重新打包

2. **创建简单的测试项目**
   - 使用 Android Studio 创建新项目
   - 复制我们的代码文件
   - 构建并测试

3. **使用 AIDE (Android IDE for mobile)**
   - 在手机上安装 AIDE
   - 直接在手机上编辑和构建

## 获取帮助

如果遇到问题：
1. 检查 Android Studio 的 Event Log
2. 查看 `gradlew build --stacktrace` 的详细输出
3. 参考 Android 官方文档 (API 19)

祝你构建顺利！
