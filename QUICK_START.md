
# ⚡ 快速上手指南 - 5分钟获取APK

**最简单的方式：使用 GitHub Actions**

---

## 📋 准备工作

您需要：
1. 一个 GitHub 账号（免费注册）
2. 网络连接
3. 我们的项目文件（已经在 `d:\车机应用\NissanCarLauncher`）

---

## 🚀 三个步骤获取APK

### 第一步：创建 GitHub 仓库

1. 访问 https://github.com 并登录
2. 点击右上角 "+" → "New repository"
3. 仓库名填：`NissanCarLauncher`
4. 选择 Public 或 Private
5. **不要**勾选 "Initialize this repository"
6. 点击 "Create repository"

### 第二步：上传代码

#### 如果您会用 Git（推荐）

```bash
cd d:\车机应用\NissanCarLauncher
git init
git add .
git commit -m "First commit"
git remote add origin https://github.com/您的用户名/NissanCarLauncher.git
git branch -M main
git push -u origin main
```

#### 如果您不会用 Git（最简单）

1. 在 GitHub 仓库页面，点击 "uploading an existing file"
2. 打开 `d:\车机应用\NissanCarLauncher` 文件夹
3. 选中所有文件和文件夹（按 Ctrl+A）
4. 拖拽到浏览器上传区域
5. 在底部填写 "Initial commit"
6. 点击 "Commit changes"

### 第三步：下载 APK！

1. **等待 2-5 分钟** ⏱️
2. 点击 GitHub 仓库顶部的 "Actions" 标签
3. 点击最新的构建工作流（应该显示绿色勾号）
4. 向下滚动找到 "Artifacts" 部分
5. 点击 `nissan-car-launcher-debug` 下载
6. 解压 ZIP 文件，得到 APK！

---

## 📱 安装到车机

### 方法 1：U盘安装（最简单）

1. 将 APK 复制到 U盘
2. 插入车机 USB 接口
3. 用车机文件管理器打开
4. 点击安装

### 方法 2：ADB 安装（需要电脑）

```bash
# 连接车机（需要知道车机IP）
adb connect 车机IP地址:5555

# 安装APK
adb install 下载的APK文件名.apk
```

---

## 💡 常见问题

**Q: 为什么我的构建显示红色叉号？**
A: 点击进入查看详细日志，通常是网络问题，重试一次即可。

**Q: 下载的 ZIP 怎么用？**
A: 解压 ZIP，里面就是 APK 文件，直接安装。

**Q: GitHub Actions 是免费的吗？**
A: 是的！公共仓库完全免费，私有仓库每月有免费额度。

**Q: 构建需要多长时间？**
A: 通常 2-5 分钟，取决于网络速度。

---

## 📖 需要更多帮助？

查看详细文档：
- `GITHUB_BUILD_GUIDE.md` - GitHub Actions 完整指南
- `BUILD_GUIDE.md` - 其他构建方式
- `README.md` - 项目说明
- `PROJECT_SUMMARY.md` - 项目总结

---

**祝使用愉快！🚗✨**
