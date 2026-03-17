# 翻页时钟 (Flip Clock)

一个简洁、无广告的安卓翻页时钟应用，模拟真实翻页时钟的效果，支持横竖屏自适应。

## 功能特性

- ✅ 3D翻页动画效果 - 带立体渐变和阴影效果
- ✅ 显示小时和分钟
- ✅ 闪烁冒号动画
- ✅ 全屏沉浸式模式
- ✅ 深色主题
- ✅ 横竖屏自适应布局
- ✅ 无广告

## 技术栈

- **开发语言**：Kotlin
- **UI框架**：Android原生View系统
- **动画实现**：ValueAnimator + Camera 3D变换
- **布局**：ConstraintLayout + LinearLayout
- **最低SDK**：API 24 (Android 7.0)
- **目标SDK**：API 36

## 项目结构

```
clock/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/example/flipclock/
│   │   │   ├── MainActivity.kt       # 主活动
│   │   │   ├── FlipDigit.kt          # 翻页数字组件
│   │   │   └── BlinkingColon.kt      # 闪烁冒号组件
│   │   └── res/
│   │       ├── layout/
│   │       │   └── activity_main.xml     # 竖屏布局
│   │       ├── layout-land/
│   │       │   └── activity_main.xml     # 横屏布局
│   │       ├── drawable/
│   │       │   └── bg_gradient.xml       # 渐变背景
│   │       └── values/
│   │           ├── strings.xml           # 字符串资源
│   │           └── themes.xml            # 主题样式
│   └── build.gradle                      # 模块构建配置
├── docs/
│   ├── 需求分析.md
│   ├── 设计文档.md
│   └── 可行性分析.md
├── BUILD_GUIDE.md                        # 构建指南
├── build.gradle                          # 项目构建配置
└── settings.gradle                       # 项目设置
```

## 安装和运行

### 前提条件

- Android Studio (推荐最新版本)
- JDK 8 或更高版本
- Android SDK (API 24+)

### 步骤

1. 克隆或下载项目到本地
2. 在Android Studio中打开项目
3. 同步Gradle依赖
4. 构建并运行应用到Android设备或模拟器

详细构建说明请参阅 [BUILD_GUIDE.md](BUILD_GUIDE.md)

## 使用说明

1. **查看时间**：应用启动后会自动全屏显示当前时间
2. **横竖屏切换**：旋转设备自动切换布局
3. **翻页效果**：时间变化时，数字会以3D翻页形式更新

## 核心组件

### FlipDigit

翻页数字自定义View，实现3D翻转动画效果：
- 使用 `Camera` 类实现3D透视变换
- 分上下两半卡片独立绘制
- 带渐变背景模拟光照效果
- 中缝凹陷阴影增强立体感
- 使用 `OvershootInterpolator` 实现轻微回弹效果

### BlinkingColon

闪烁冒号组件：
- 使用 `ObjectAnimator` 实现透明度渐变
- 1秒周期平滑闪烁
- 带阴影效果

## 自定义

### 调整翻页动画速度

在 `FlipDigit.kt` 文件中修改动画持续时间：

```kotlin
duration = 800  // 毫秒
```

### 调整动画回弹效果

```kotlin
interpolator = OvershootInterpolator(0.8f)  // 数值越大回弹越明显
```

### 调整卡片颜色

在 `FlipDigit.kt` 的 `onSizeChanged` 方法中修改渐变颜色：

```kotlin
topCardPaint.shader = LinearGradient(
    0f, 0f, 0f, viewHeight / 2,
    Color.parseColor("#484848"),  // 顶部颜色
    Color.parseColor("#383838"),  // 底部颜色
    Shader.TileMode.CLAMP
)
```

## 未来计划

- [ ] 添加秒钟显示开关
- [ ] 添加更多主题选项
- [ ] 实现屏幕常亮功能
- [ ] 添加闹铃功能
- [ ] 支持秒表和计时器
- [ ] 添加12/24小时制切换

## 许可证

本项目采用 MIT 许可证 - 详见 LICENSE 文件

## 贡献

欢迎提交 Issue 和 Pull Request！
