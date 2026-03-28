# BetterMapEditor 根层说明

## 这一层负责什么
仓库根目录是整个模组项目的编排层。它不直接实现地图编辑器功能，而是把“功能代码在哪里”“如何构建”“如何发布”“模组在游戏里怎样被识别”这些全局问题统一收口。

这一层同时承担三个角色：

- 项目入口层：通过 `mod.json` 告诉 Mindustry 这个模组叫什么、主类是谁、最低游戏版本是多少。
- 构建控制层：通过 `build.gradle` 和 `settings.gradle` 定义 Java 8 兼容性、Mindustry 依赖、桌面与 Android 产物、发布复制逻辑。
- 人类协作层：通过 `README.md`、`CHANGELOG.md`、`AGENTS.md`、`.gitignore` 说明用法、版本历史、协作约束和哪些目录是生成物。

## 本层已有文件

- `build.gradle`
  整个仓库最重要的工程脚本。它定义依赖 `MindustryJitpack:core`、替换 Arc 版本解析策略、产出 `jar`/`zip`/`android jar`，并把合并后的成品复制到 `dist/` 和上级 `构建/` 目录。
- `settings.gradle`
  只做一件事：定义 Gradle 项目名 `betterMapEditor`。这个名字会影响默认任务命名、输出文件名基准和 IDE 工程识别。
- `mod.json`
  Mindustry 模组元数据。运行时由游戏先读取它，再根据 `main` 字段实例化 `bettermapeditor.BetterMapEditorMod`。
- `README.md`
  面向使用者和开发者的总说明。它描述模组目标、拖拽镜像轴的功能、安装方式和基本构建命令。
- `CHANGELOG.md`
  面向发布历史。能反推出当前代码为什么有反射兼容、类加载器问题规避、镜像滤镜自动替换等设计。
- `.gitignore`
  把 `.gradle/`、`build/`、`out/`、IDE 文件排除出版本控制，说明这些目录是工具输出，不是源码层。
- `AGENTS.md`
  当前工作区的自动化协作约束，规定 Java 8 兼容、优先资源化文案、命令使用 `pwsh` 等边界。

## 本层目录之间的关系

- `.github/`
  发布自动化层。根层把版本和构建规则给它，它再在 GitHub Actions 中复现同样的构建流程。
- `src/`
  真正的源码与资源层。根层只定义它是 `main` source set，具体功能逻辑都在这里。
- `dist/`
  发布产物暂存层。根层的 Gradle 任务会把最终可分发文件复制到这里。

## 关键实现方式

- 通过 `mod.json + main class` 形成游戏加载入口。
- 通过 `compileOnly` 依赖 Mindustry 核心，保证编译时可见、发布时不把游戏本体打进模组。
- 通过 `jarMerged` 与 `zipMerged` 把桌面类文件、资源文件、`classes.dex`、`mod.json` 合并到单文件发布物中。
- 通过 `copyMergedJarToDist` 和 `copyMergedZipToDist` 让仓库内部保留一份随手可发的成品。

## 与其他层级的调用链

- 游戏启动时，根层声明的 `mod.json` 把控制权交给 `src/main/java/bettermapeditor/BetterMapEditorMod.java`。
- 构建时，根层的 Gradle 任务读取 `src/main/java` 和 `src/main/resources`，产出 `dist/` 的最终文件。
- 发布时，`.github/workflows/release.yml` 调用这里定义的 `deploy` 任务，而不是重复写另一套打包逻辑。

## 特别说明

- `.git/` 是 Git 内部数据库与引用管理目录，改写它会干扰版本库状态，所以不为其生成说明文件。
- `.gradle/` 与 `build/` 是本地构建缓存和中间产物，它们的内容由工具决定、可重建，不属于项目长期维护结构，因此本次不写入 `Detail.md`。
