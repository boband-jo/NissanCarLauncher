
# 使用 GitHub Actions 构建 APK 指南

✨ 完全免费，无需配置本地开发环境！

## 快速开始

### 第一步：创建 GitHub 仓库

1. **注册 GitHub 账号**（如果还没有）
   - 访问 https://github.com
   - 免费注册一个账号

2. **创建新仓库**
   - 点击页面右上角的 "+" → "New repository"
   - 仓库名称: `NissanCarLauncher` (或您喜欢的名字)
   - 选择 "Public" 或 "Private"
   - **不要**勾选 "Initialize this repository"（我们稍后会上传代码）
   - 点击 "Create repository"

### 第二步：上传代码到 GitHub

#### 方法 A：使用 Git 命令行（推荐）

```bash
# 1. 进入项目目录
cd d:\车机应用\NissanCarLauncher

# 2. 初始化 Git 仓库
git init

# 3. 添加所有文件
git add .

# 4. 提交更改
git commit -m "Initial commit: Nissan Car Launcher"

# 5. 关联远程仓库（替换为您的仓库地址）
git remote add origin https://github.com/您的用户名/NissanCarLauncher.git

# 6. 推送到 GitHub
git branch -M main
git push -u origin main
```

#### 方法 B：使用 GitHub Desktop（图形界面）

1. 下载并安装 GitHub Desktop: https://desktop.github.com
2. 打开 GitHub Desktop
3. 选择 "File" → "Add Local Repository"
4. 选择 `d:\车机应用\NissanCarLauncher` 文件夹
5. 点击 "Publish repository"

#### 方法 C：直接上传文件（最简单）

1. 在 GitHub 仓库页面，点击 "uploading an existing file"
2. 拖拽整个 `NissanCarLauncher` 文件夹到页面
3. 提交更改

### 第三步：自动构建 APK！

🎉 代码推送到 GitHub 后，**GitHub Actions 会自动开始构建！**

您只需要：

1. 打开您的 GitHub 仓库
2. 点击顶部的 "Actions" 标签页
3. 看到 "Build Nissan Car Launcher APK" 工作流正在运行
4. 等待 2-5 分钟，构建完成！

### 第四步：下载构建好的 APK

1. 在 Actions 页面，点击最新的构建工作流
2. 向下滚动到 "Artifacts" 部分
3. 点击下载：
   - `nissan-car-launcher-debug` - Debug版本APK（推荐测试用）
   - `nissan-car-launcher-release-unsigned` - Release版本APK
4. 下载的是一个 ZIP 文件，解压后即可得到 APK

## 详细说明

### 工作流触发方式

GitHub Actions 会在以下情况自动运行：

1. **推送代码到 main/master 分支** - 自动构建
2. **创建 Pull Request** - 自动构建验证
3. **手动触发** - 在 Actions 页面点击 "Run workflow"

### 手动触发构建

1. 进入仓库的 "Actions" 页面
2. 点击 "Build Nissan Car Launcher APK"
3. 点击 "Run workflow" 按钮
4. 选择分支，点击 "Run workflow"

### 构建产物

每次构建会生成两个 APK：

#### Debug APK (`nissan-car-launcher-debug`)
- ✅ 已使用调试密钥签名
- ✅ 可以直接安装到车机
- ✅ 适合测试和开发使用

#### Release APK (`nissan-car-launcher-release-unsigned`)
- ⚠️ 未签名，需要自己签名后才能安装
- ✅ 适合发布和正式使用

## 高级配置（可选）

### 设置自动发布 Release

如果您想让 GitHub 在每次推送到 main 分支时自动创建 Release：

1. 工作流已经配置了自动创建 Release
2. 每次推送到 main 分支，会自动创建 Release 并上传 APK
3. Release 会显示在仓库首页右侧

### 添加签名密钥（可选）

如果您想让 GitHub Actions 自动签名 Release APK：

1. **生成密钥库**
```bash
keytool -genkey -v -keystore release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias release
```

2. **将密钥转换为 Base64**
```bash
base64 release-key.jks &gt; release-key.jks.base64
```

3. **在 GitHub 仓库添加 Secrets**
   - 进入仓库 → Settings → Secrets and variables → Actions
   - 添加以下 Secrets：
     - `KEYSTORE_FILE`: Base64 编码的密钥库内容
     - `KEYSTORE_PASSWORD`: 密钥库密码
     - `KEY_ALIAS`: 密钥别名
     - `KEY_PASSWORD`: 密钥密码

4. **修改工作流文件** 添加签名步骤

### 自定义构建版本

手动触发构建时，可以在输入框中指定版本号。

## 故障排除

### 问题 1：构建失败

**检查步骤：**
1. 点击 Actions 页面的失败工作流
2. 查看详细错误日志
3. 常见问题：
   - 缺少某些文件
   - 依赖下载失败（重试一次）
   - Gradle 配置问题

### 问题 2：找不到 Artifacts

**解决：**
1. 确保构建已完成（绿色勾号）
2. 向下滚动到页面底部
3. Artifacts 在 Summary 部分

### 问题 3：APK 安装失败

**Debug APK 安装：**
- Debug 版本应该可以直接安装
- 如果不行，尝试卸载旧版本后重新安装

**Release APK 安装：**
- Release 版本未签名，需要先签名
- 使用 `apksigner` 或 `jarsigner` 签名

## 完整示例：从零开始

```bash
# 1. 假设您在 d:\车机应用 目录
cd d:\车机应用

# 2. 复制 NissanCarLauncher 到临时目录（避免影响原文件）
xcopy NissanCarLauncher NissanCarLauncher-Git /E /I

# 3. 进入新目录
cd NissanCarLauncher-Git

# 4. 初始化 Git
git init

# 5. 添加文件
git add .

# 6. 提交
git commit -m "Initial commit"

# 7. 关联 GitHub 仓库（替换为您的仓库）
git remote add origin https://github.com/您的用户名/NissanCarLauncher.git

# 8. 推送
git branch -M main
git push -u origin main

# 9. 去 GitHub Actions 页面看构建！
```

## 优势对比

| 特性 | 本地构建 | GitHub Actions 构建 |
|------|---------|-------------------|
| 需要安装 Android Studio | ✅ | ❌ |
| 需要配置环境 | ✅ | ❌ |
| 跨平台可用 | ⚠️ | ✅ |
| 构建历史记录 | ⚠️ | ✅ |
| 自动构建 | ⚠️ | ✅ |
| 完全免费 | ✅ | ✅ |

## 额外提示

💡 **建议先使用 Debug APK 测试**，稳定后再使用 Release 版本

💡 **可以创建多个分支** 进行不同功能的测试

💡 **利用 GitHub Releases** 管理版本发布

💡 **查看 Actions 使用情况** 在仓库 Settings → Billing and plans

## 获取帮助

如果遇到问题：
1. 查看 Actions 页面的构建日志
2. 检查 GitHub Actions 文档: https://docs.github.com/actions
3. 搜索相关问题的解决方案

---

祝使用愉快！🚗✨
