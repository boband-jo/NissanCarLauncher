
# Gradle Wrapper 说明

## 重要：获取 gradle-wrapper.jar

由于 `gradle-wrapper.jar` 是二进制文件，您需要通过以下方式之一获取它：

### 方法一：使用本地 Gradle 生成（推荐）

如果您已安装 Gradle：

```bash
cd d:\车机应用\NissanCarLauncher
gradle wrapper
```

### 方法二：从其他项目复制

从任何标准的 Android/Gradle 项目中复制 `gradle/wrapper/gradle-wrapper.jar` 到这里。

### 方法三：使用 Android Studio

1. 用 Android Studio 打开项目
2. Gradle 会自动生成 wrapper 文件

### 方法四：手动下载

从 Gradle 官方仓库下载：
- Gradle 2.14.1: https://services.gradle.org/distributions/gradle-2.14.1-bin.zip
- 解压后，从 `lib/gradle-wrapper-*.jar` 获取

---

## 文件说明

本目录应有以下文件：

```
gradle/wrapper/
├── gradle-wrapper.jar      ⚠️ 需要您添加
├── gradle-wrapper.properties (已创建)
└── README.md                (本文件)
```

---

## GitHub Actions 说明

好消息：GitHub Actions 实际上不需要本地的 gradle-wrapper.jar！

GitHub Actions 会使用我们在工作流中配置的 Gradle 版本，直接下载所需版本。

但为了项目完整性，建议还是添加 gradle-wrapper.jar 文件。
