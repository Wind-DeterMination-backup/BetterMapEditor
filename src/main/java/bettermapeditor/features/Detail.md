# bettermapeditor.features 层说明

## 这一层负责什么
这一层是功能编排层。它不直接定义模组元数据，也不直接实现镜像数学，而是负责把游戏 UI、滤镜替换、拖拽输入、预览刷新这些步骤串成一个持续工作的功能链。

## 本层文件

- `DraggableMirrorAxisFeature.java`
  全模组最核心的运行时协调器。

## DraggableMirrorAxisFeature 做了什么

- 在初始化阶段替换 `Maps.allFilterTypes` 中的原版 `MirrorFilter` 提供器，使新创建的镜像滤镜直接变成 `DraggableMirrorFilter`。
- 在客户端完成加载后，再次执行补丁并解析 `MapGenerateDialog` 的私有字段和方法，防止加载顺序导致第一次补丁失效。
- 在每帧 `Trigger.update` 中检查地图生成对话框是否打开、滤镜列表是否需要替换、预览图片是否已经找到、拖拽监听器是否已挂上。

## 关键实现细节

- 反射绑定
  通过 `filters` 字段拿到当前生成器滤镜列表，通过 `rebuildFilters()` 重建右侧滤镜 UI，通过 `update()` 刷新预览。
- 旧滤镜迁移
  如果用户打开的是已有地图或旧配置，列表里可能已经存在原版 `MirrorFilter`。这一层会把它们逐个替换成 `DraggableMirrorFilter.fromMirror(...)`，保留原有 `seed`、`angle`、`rotate`。
- 预览图定位
  对 `MapGenerateDialog.cont` 进行广度搜索，寻找面积和 drawable 评分最大的 `Image`，作为最可能的地图预览控件。
- 监听器热附着
  一旦预览控件对象变化，就先卸载旧监听器，再把新的 `AxisDragListener` 挂上去，避免重复监听或悬挂引用。
- 刷新节流
  拖拽时以约 33ms 的最小间隔刷新预览，兼顾实时反馈和性能。

## AxisDragListener 的职责

- `touchDown`
  在用户按下时判断是否命中任一镜像轴。
- `touchDragged`
  持续把鼠标位置转换为归一化轴心坐标。
- `touchUp`
  结束拖拽并强制刷新最终状态。

## 命中判定方法

- 先用 `DraggableMirrorFilter.computePreviewRect` 计算预览图中真正代表地图的绘制矩形。
- 再按每个镜像滤镜的 `axisXNorm/axisYNorm` 还原轴心位置。
- 用轴方向向量计算鼠标到轴线的垂直距离，取最近者。
- 若只有一个镜像滤镜，则允许在扩展后的整个地图矩形内直接抓取，提高可用性。

## 与其他层级的关系

- 上游由 `BetterMapEditorMod` 调用 `init()` 启动。
- 下游直接依赖 `mindustry.maps.filters.DraggableMirrorFilter` 提供坐标、绘制、镜像算法。
- 它是 UI 与滤镜之间的桥接层：既懂 `MapGenerateDialog`，也懂镜像滤镜的数据结构。
