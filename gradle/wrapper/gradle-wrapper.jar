
# Gradle Wrapper JAR 文件说明

## 重要提示

`gradle-wrapper.jar` 文件是必需的，但是由于它是二进制文件，我们在这里提供几种获取方式：

---

## 方式一：使用 Android Studio 生成（最简单）

如果您有 Android Studio：

1. 用 Android Studio 打开项目
2. 等待 Gradle 同步完成
3. Android Studio 会自动生成 `gradle-wrapper.jar`

---

## 方式二：使用 Gradle 命令生成

如果您已安装 Gradle：

```bash
# 在项目根目录运行
gradle wrapper --gradle-version 2.14.1
```

---

## 方式三：手动下载（快速）

1. 访问以下任一地址下载：
   - https://raw.githubusercontent.com/gradle/gradle/v2.14.1/gradle/wrapper/gradle-wrapper.jar
   - 或者从其他 Gradle 项目复制

2. 将下载的文件放到：
   ```
   NissanCarLauncher/gradle/wrapper/gradle-wrapper.jar
   ```

---

## 方式四：使用 GitHub Actions（推荐）

**好消息！** 我们的 GitHub Actions 工作流已经配置为不需要本地 gradle-wrapper.jar！

- GitHub Actions 会自动处理 Gradle Wrapper
- 您可以直接上传项目到 GitHub
- GitHub 会在云端完成构建

---

## 文件信息

- **Gradle 版本**: 2.14.1
- **目的**: 确保所有环境使用相同的 Gradle 版本
- **位置**: `gradle/wrapper/gradle-wrapper.jar`

---

## 验证

当您有了 `gradle-wrapper.jar` 后，项目结构应该是：

```
NissanCarLauncher/
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar      ← 这个文件现在应该有了
│       └── gradle-wrapper.properties
├── gradlew                          ← Linux/Mac 脚本
├── gradlew.bat                      ← Windows 脚本
└── ...
```

---

## 下一步

获取到 `gradle-wrapper.jar` 后：

1. 将所有文件提交到 Git
2. 推送到 GitHub
3. GitHub Actions 会自动构建！

或者，如果您使用 GitHub Actions 构建，可以忽略这个文件，工作流会自动处理。
