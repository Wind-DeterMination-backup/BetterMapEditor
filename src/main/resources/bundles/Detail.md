# bundles 层说明

## 这一层负责什么
这一层是模组的文本字典层，负责所有当前已经资源化的用户可见文案。它是代码和最终显示文本之间的翻译表。

## 本层文件

- `bundle.properties`
  默认语言资源，目前内容偏英文。
- `bundle_zh_CN.properties`
  简体中文资源。

## 已覆盖的 key

- `mod.bettermapeditor.name`
  模组显示名。
- `mod.bettermapeditor.description`
  模组说明，会在模组列表等位置展示。
- `settings.bettermapeditor`
  设置分类标题。
- `setting.bme-updatecheck.name`
  更新检查开关名称。
- `setting.bme-updatecheck.description`
  更新检查开关说明。
- `setting.bme-updatecheck-dialog.name`
  更新弹窗开关名称。
- `setting.bme-updatecheck-dialog.description`
  更新弹窗开关说明。

## 实现方式

- Java 代码只引用 key，例如 `@settings.bettermapeditor`。
- 游戏根据当前语言环境选择匹配的 bundle 文件。
- 如果某个语言文件缺 key，通常会回退到默认 bundle，因此 `bundle.properties` 兼具兜底职责。

## 与其他层级的关系

- 上层 `resources/` 提供资源打包入口。
- 被 `mod.json` 的元数据展示和 `BetterMapEditorMod` 注册设置界面时间接消费。
- 它与 `README.md` 不同：`README` 面向仓库访问者，这里的文本面向游戏内用户。

## 当前资源化程度

- 设置相关文案已经资源化，符合仓库约束。
- `GithubUpdateCheck` 弹窗正文仍然是硬编码英文字符串，说明项目的资源化策略已建立，但尚未覆盖到全部运行时提示文本。
