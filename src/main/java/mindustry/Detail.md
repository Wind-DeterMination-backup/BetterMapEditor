# mindustry 包镜像层说明

## 这一层负责什么
这一层是“对上游包路径进行本地扩展”的适配层。它不是完整复制 Mindustry 源码，而是只放那些必须与上游类放在相近语义位置、才能自然参与原版流程的类。

## 为什么要用上游包名

- `DraggableMirrorFilter` 是对 `MirrorFilter` 的增强版，而不是完全独立的新系统。
- 放在 `mindustry.maps.filters` 下，阅读时能立即看出它与地图生成滤镜体系的关系。
- 这种布局也让 `DraggableMirrorAxisFeature` 替换 `Maps.allFilterTypes` 时更容易表达“原版镜像滤镜被同位替换”。

## 结构特点

- 当前只包含 `maps/` 分支，说明侵入范围被刻意控制在地图生成器相关逻辑。
- 没有把更多 UI、实体、世界逻辑塞进来，避免项目变成对上游命名空间的大面积污染。

## 与其他层级的关系

- 上层是 `src/main/java/` 的第二条主分支。
- 下层 `maps/` 继续缩小范围，只承载地图相关增强。
- 它被 `bettermapeditor.features` 调用，但不反过来依赖模组入口层，从而保持“底层实现可复用、上层负责装配”的方向。
