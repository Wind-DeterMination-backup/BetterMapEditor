# bettermapeditor 包层说明

## 这一层负责什么
`bettermapeditor` 是模组自有主包，负责把多个零散能力组织成一个可加载、可配置、可自检更新的客户端模组。

## 本层文件与职责

- `BetterMapEditorMod.java`
  模组主入口，继承 `mindustry.mod.Mod`。它在 `init()` 中初始化拖拽镜像轴功能，并在客户端加载完成后注册设置项和更新检查。
- `GithubUpdateCheck.java`
  GitHub 更新检查服务。负责节流、版本比较、GitHub API 访问、失败回退到原始 `mod.json`、弹窗或 toast 提示。
- `features/`
  放真正的功能编排器。
- `filters/`
  目前为空，说明作者预留过“自有滤镜命名空间”的扩展位置，但当前实现最终选择把核心滤镜类放进 `mindustry` 包路径。

## BetterMapEditorMod 的实现方式

- 启动即调用 `DraggableMirrorAxisFeature.init()`，确保功能挂接尽早完成。
- 监听 `ClientLoadEvent`，因为设置 UI 与联网检查都需要在客户端 UI 完整初始化后再做。
- 通过 `settingsAdded` 防止设置分类重复注册。
- 使用 bundle key `@settings.bettermapeditor` 作为分类标题，避免硬编码用户可见文本。

## GithubUpdateCheck 的实现方式

- 使用 `Core.settings.defaults` 写入默认开关。
- 使用 `checked` 静态标志避免同一进程重复检查。
- 使用 6 小时间隔控制请求频率，减少每次打开游戏都访问 GitHub。
- 优先请求 `releases/latest`，失败时回退到仓库 `main` 分支的 `mod.json`。
- 使用正则提取纯数字版本段，避免 `v1.1.2` 这类前缀影响比较。
- UI 层支持两种反馈：完整对话框，或只显示 toast。

## 与其他层级的关系

- 向上由 `mod.json` 的 `main` 字段加载。
- 向下通过 `features/` 驱动地图生成器增强功能。
- 依赖 `resources/bundles` 提供设置界面的标题和描述。
- 通过 `mindustry.maps.filters.DraggableMirrorFilter` 把高层入口和底层滤镜逻辑连接起来。
