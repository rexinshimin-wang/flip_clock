# 翻页时钟应用 - 构建和运行指南

## 方法一：使用Android Studio（推荐）

### 前提条件
- 已安装Android Studio
- 已安装JDK 8或更高版本
- 已配置Android SDK

### 步骤1：打开项目
1. 启动Android Studio
2. 选择 "Open an Existing Project"
3. 导航到 `c:\Users\23624\OneDrive\桌面\clock` 目录
4. 点击 "OK"

### 步骤2：同步Gradle依赖
1. Android Studio会自动检测到Gradle文件
2. 顶部会显示 "Gradle sync needed" 提示
3. 点击 "Sync Now" 按钮
4. 或者点击菜单：`File` -> `Sync Project with Gradle Files`
5. 等待同步完成（底部状态栏会显示进度）

### 步骤3：构建项目
1. 点击菜单：`Build` -> `Make Project`
2. 或者使用快捷键：`Ctrl + F9`
3. 等待构建完成

### 步骤4：运行应用
1. 连接您的Android设备（开启USB调试模式）
2. 点击 "Run" -> "Run 'app'"
3. 选择您的设备并点击 "OK"
4. 应用将安装并运行在您的设备上

## 方法二：使用命令行

### 前提条件
- 已安装JDK 8或更高版本
- 已配置JAVA_HOME环境变量
- 已安装Android SDK

### 步骤1：下载Gradle Wrapper
由于项目缺少gradle-wrapper.jar文件，您需要：

1. 从Gradle官方网站下载gradle-wrapper.jar文件
2. 将文件放置在 `gradle\wrapper\` 目录下

或者使用以下命令生成Gradle Wrapper：
```bash
gradle wrapper --gradle-version 8.0
```

### 步骤2：同步Gradle依赖
```bash
cd c:\Users\23624\OneDrive\桌面\clock
gradlew.bat build
```

### 步骤3：构建APK
```bash
gradlew.bat assembleDebug
```

生成的APK文件位置：`app\build\outputs\apk\debug\app-debug.apk`

### 步骤4：安装APK到设备
```bash
adb install app\build\outputs\apk\debug\app-debug.apk
```

## 常见问题

### 1. Gradle同步失败
- 检查网络连接
- 检查Gradle版本是否正确
- 尝试清除缓存：`File` -> `Invalidate Caches / Restart`

### 2. 构建失败
- 检查JDK版本
- 检查Android SDK是否正确配置
- 查看错误日志，根据错误信息解决问题

### 3. 无法连接设备
- 确保USB调试已开启
- 检查USB连接是否正常
- 尝试重新连接设备

## 推荐使用方法

**强烈推荐使用Android Studio**，因为：
- 自动处理Gradle依赖
- 提供图形化界面
- 更容易调试和测试
- 自动生成必要的Gradle Wrapper文件

## 获取帮助

如果遇到问题：
1. 查看Android Studio的错误日志
2. 检查项目文档：`docs/` 目录
3. 查看项目说明：`README.md`
