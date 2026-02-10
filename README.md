# BetterMapEditor

一个专注于 **Mindustry 地图编辑器自动生成体验** 的客户端模组。  
This is a client-side mod focused on improving the **Mindustry map generator workflow**.

## 中文说明

### 功能
- 在地图编辑器 `自动生成` 预览中，支持直接拖动 `镜像` 滤镜的对称轴。
- 保留原版镜像参数（角度 / rotate）并增加轴心位置控制。
- 支持已存在镜像滤镜的自动替换，无需手动迁移配置。

### 使用方式
1. 打开地图编辑器，进入 `自动生成`。
2. 添加或选中 `镜像` 滤镜。
3. 在左侧预览图中按住并拖动镜像轴（高亮线）即可移动对称轴。

### 安装
- 从 Releases 下载产物：
  - `betterMapEditor-<version>.zip`
  - `betterMapEditor-<version>.jar`
  - `betterMapEditor-<version>-android.jar`
- 放入 Mindustry 的 `mods` 目录并重启游戏。

### 开发构建
```bash
gradle deploy
```

## English

### Features
- Drag the mirror symmetry axis directly in the map generator preview.
- Keeps original mirror options (`angle` / `rotate`) while adding axis position control.
- Automatically upgrades existing mirror filters in the generator list.

### Usage
1. Open the map editor and go to `Generate`.
2. Add/select the `Mirror` filter.
3. Drag the highlighted mirror axis in the preview to move the symmetry line.

### Install
- Download one of these artifacts from Releases:
  - `betterMapEditor-<version>.zip`
  - `betterMapEditor-<version>.jar`
  - `betterMapEditor-<version>-android.jar`
- Put it into your Mindustry `mods` directory and restart the game.

### Build
```bash
gradle deploy
```
