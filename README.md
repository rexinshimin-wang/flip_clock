# 翻页时钟 (Flip Clock)

一个简洁、无广告的安卓翻页时钟应用，模拟真实翻页时钟的效果。

## 功能特性

- ✅ 翻页动画效果
- ✅ 显示小时、分钟、秒钟
- ✅ AM/PM 显示
- ✅ 可选择是否显示秒钟
- ✅ 深色主题
- ✅ 无广告

## 技术栈

- **开发语言**：Kotlin
- **UI框架**：Android原生View系统
- **动画实现**：ObjectAnimator
- **布局**：ConstraintLayout

## 项目结构

```
clock/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/example/flipclock/
│   │   │   │   ├── MainActivity.kt      # 主活动
│   │   │   │   └── FlipDigit.kt         # 翻页数字组件
│   │   │   └── res/
│   │   │       ├── layout/
│   │   │       │   ├── activity_main.xml    # 主布局
│   │   │       │   └── dialog_settings.xml  # 设置对话框
│   │   │       ├── menu/
│   │   │       │   └── menu_main.xml        # 菜单
│   │   │       └── values/
│   │   │           ├── strings.xml          # 字符串资源
│   │   │           └── themes.xml           # 主题样式
│   │   └── build.gradle                     # 模块构建配置
├── build.gradle                             # 项目构建配置
└── settings.gradle                          # 项目设置
```

## 安装和运行

### 前提条件

- Android Studio
- Android SDK (API 24+)
- Kotlin插件

### 步骤

1. 克隆或下载项目到本地
2. 在Android Studio中打开项目
3. 同步Gradle依赖
4. 构建并运行应用到Android设备或模拟器

## 使用说明

1. **查看时间**：应用启动后会自动显示当前时间
2. **设置**：点击左上角的设置图标，在弹出的对话框中可以选择是否显示秒钟
3. **翻页效果**：时间变化时，数字会以翻页的形式更新

## 自定义

### 调整翻页动画速度

在 `FlipDigit.kt` 文件中修改动画持续时间：

```kotlin
// 修改这里的数值（毫秒）
animator.duration = 500
```

### 调整数字样式

在 `FlipDigit.kt` 文件中修改文字大小和颜色：

```kotlin
private val paint = Paint().apply {
    color = Color.WHITE
    textSize = 60f
    // 其他设置...
}
```

## 未来计划

- [ ] 添加更多主题选项
- [ ] 实现屏幕常亮功能
- [ ] 添加闹铃功能
- [ ] 支持秒表和计时器
- [ ] 添加天气信息显示

## 许可证

本项目采用 MIT 许可证 - 详见 LICENSE 文件

## 贡献

欢迎提交 Issue 和 Pull Request！
