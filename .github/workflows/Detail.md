# .github/workflows 层说明

## 这一层负责什么
这一层是持续交付脚本层。它把仓库里的“可构建项目”变成“可发布版本”，核心职责是复现本地构建环境、产出发布文件并创建 GitHub Release。

## 本层文件

- `release.yml`
  唯一工作流文件。监听 `workflow_dispatch` 和 `push tags: v*`，说明发布既支持手动触发，也支持按语义版本标签自动触发。

## release.yml 的实现逻辑

工作流按顺序完成以下事情：

- 检出仓库代码，确保 runner 上有完整源码。
- 安装 Java 17。这里用较新的 JDK 跑 Gradle，不影响最终字节码，因为真正的兼容性由 `build.gradle` 中的 `release 8` 保证。
- 安装 Gradle 缓存支持，减少重复下载依赖的成本。
- 安装 Android SDK 与 build-tools 34.0.0，为 `d8` 生成 `classes.dex` 提供环境。
- 执行 `clean deploy`，让构建逻辑仍然完全由 Gradle 维护。
- 把 `dist/*.jar` 与 `dist/*.zip` 上传到 GitHub Release。

## 为什么这样实现

- `deploy` 已经封装了“合并桌面和 Android 产物”的规则，工作流只需调用一次，避免 CI 和本地构建分叉。
- `dist` 被选作上传目录，意味着 CI 与本地手工发布使用同一套输出位置。
- 使用 tag 触发而不是 branch 触发，可以把“普通提交”和“正式发布”清晰分开。

## 与其他层级的关系

- 依赖根层的 `build.gradle` 定义具体任务。
- 间接消费 `src/main/java`、`src/main/resources`，因为它们会在构建时被编译和打包。
- 输出结果最终回流到 `dist/` 这一发布层，再被 GitHub Release 接走。
