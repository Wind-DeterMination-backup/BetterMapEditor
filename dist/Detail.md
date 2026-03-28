# dist 层说明

## 这一层负责什么
`dist/` 是仓库内的发布成品层。这里不放源码，也不放中间缓存，而是放已经适合发给玩家、上传 Release 或复制到 `mods` 目录的最终文件。

## 本层文件

- `BetterMapEditor.jar`
- `BetterMapEditor.zip`

这两个文件当前体积一致，内部条目也一致，说明它们是同一份合并产物的两种扩展名包装。

## 产物内部结构

两份文件都包含：

- `bettermapeditor/*.class`
  模组主包编译后的字节码。
- `bettermapeditor/features/*.class`
  交互增强功能的字节码。
- `mindustry/maps/filters/DraggableMirrorFilter.class`
  放在上游包路径中的自定义滤镜实现。
- `bundles/*.properties`
  运行时本地化文本。
- `classes.dex`
  Android 侧加载所需的 dex 文件。
- `mod.json`
  让 Mindustry 识别该压缩包是合法模组。

## 为什么同时保留 jar 和 zip

- Mindustry 生态里两种扩展名都常见，部分用户更习惯 `zip` 模组包。
- 某些分发平台或自动脚本会默认识别 `jar`。
- 构建脚本因此提供双份输出，降低安装与分发时的摩擦。

## 这一层如何产生

- `jarMerged` 先把桌面类、资源、`classes.dex`、`mod.json` 合成单一 jar。
- `zipMerged` 再把这个 jar 展开后重新封成 zip。
- `copyMergedJarToDist` 和 `copyMergedZipToDist` 将结果复制到本目录。

## 与其他层级的关系

- 上游直接依赖根层 Gradle 任务。
- 内容来源于 `src/main/java` 与 `src/main/resources`。
- 下游通常是 GitHub Release、用户手工安装、或其他外部分发渠道。

## 维护边界

- 这里的文件可以再生成，所以它们更像“可提交的发布快照”，不是人工手改目标。
- 如果源码更新但未重新执行 `deploy`，这一层就会落后于 `src/`，因此它的正确性取决于构建流程是否跑过。
