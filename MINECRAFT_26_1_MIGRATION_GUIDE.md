# Minecraft 26.1 遷移指南

## 概述

本文檔記錄將 DG-Lab 模組從 Minecraft 1.21.10 升級到 Minecraft 26.1 的完整遷移路線圖。

**狀態**: ⏳ 等待 Fabric Loom 1.16 發佈

## 當前版本 (1.21.10)

```properties
minecraft_version=1.21.10
yarn_mappings=1.21.10+build.3
loader_version=0.17.2
fabric_version=0.138.4+1.21.10
loom_version=1.11-SNAPSHOT
java=21
gradle=8.14
```

## 目標版本 (26.1)

```properties
minecraft_version=26.1
loader_version=0.18.4
fabric_version=0.140.0+26.1
loom_version=1.16+ (no-remap variant)
java=25
gradle=9.4.0
```

## 主要變更

### 1. 版本更新

#### Gradle 相關

- **Gradle**: 8.14 → 9.4.0
- **Fabric Loom**: 1.11 → 1.16 (no-remap variant)
  - 插件 ID: `fabric-loom` → `net.fabricmc.fabric-loom` (可選)
  - 移除 Yarn Mappings 依賴 (Mojang mappings 內建)

#### Minecraft 相關

- **Minecraft**: 1.21.10 → 26.1
- **Loader**: 0.17.2 → 0.18.4
- **Fabric API**: 0.138.4+1.21.10 → 0.140.0+26.1
- **Java**: 21 → 25

### 2. 編譯配置變更

#### build.gradle

```gradle
// 移除映射行
// mappings "net.fabricmc:yarn:${project.yarn_mappings}:v2"
// Mojang mappings 現已內建於 Loom 1.16+

// 更新 Java 版本
def targetJavaVersion = 25
```

#### gradle.properties

```properties
# 移除
# yarn_mappings=...

# 更新為 26.1
minecraft_version=26.1
loader_version=0.18.4
fabric_version=0.140.0+26.1
```

#### gradle-wrapper.properties

```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-9.4.0-bin.zip
```

### 3. fabric.mod.json 變更

```json
{
  "depends": {
    "fabricloader": ">=${loader_version}",
    "fabric-api": "*",  // 從 "fabric" 改為 "fabric-api"
    "minecraft": "~${minecraft_version}"
    // 移除 "fabric-key-binding-api-v1" (已包含在 fabric-api 中)
  }
}
```

## Mojang Mappings 遷移

### 類別名稱對應 (Yarn → Mojang)

| Yarn 名稱 | Mojang 名稱 | 影響檔案 |
|-----------|------------|---------|
| `MinecraftClient` | `Minecraft` | Mixin, 工具類 |
| `ClientPlayerEntity` | `LocalPlayer` | Mixin |
| `AbstractClientPlayerEntity` | `AbstractClientPlayer` | Mixin |
| `ClientWorld` | `ClientLevel` | Mixin |
| `ClientPlayerInteractionManager` | `MultiPlayerGameMode` | Mixin |
| `ServerPlayerEntity` | `ServerPlayer` | Mixin |
| `PlayerEntity` | `Player` | Mixin |
| `DrawContext` | `GuiGraphics` | Screen 類 (9 個檔案) |
| `Text` | `Component` | Screen, 工具類 (6 個檔案) |
| `OrderedText` | `FormattedCharSequence` | Screen 類 |
| `KeyBinding` | `KeyMapping` | Dg_labClient.java |
| `Identifier` | `ResourceLocation` | 多個類 |
| `InputUtil` | `InputConstants` | Dg_labClient.java |

### 方法/字段名稱變更

| Yarn 名稱 | Mojang 名稱 | 位置 |
|-----------|------------|------|
| `ClientPlayerEntity.updateHealth()` | `LocalPlayer.hurtTo()` | Mixin |
| `IntegratedServer.isRemote()` | `IntegratedServer.isPublished()` | tick.java |
| `LivingEntity.lastDamageTaken` | `lastHurt` | LivingEntityAccessor |
| `MinecraftClient.getServer()` | `Minecraft.getSingleplayerServer()` | tick.java |
| `ClientPlayerInteractionManager.attackEntity()` | `MultiPlayerGameMode.attack()` | Mixin |
| `Text.literal()` | `Component.literal()` | 全域 |
| `DrawContext.drawTextWithShadow()` | `GuiGraphics.drawString()` | Screen 類 |
| `KeyBinding.Category.create()` | `KeyMapping.Category.createOrGet()` | Dg_labClient.java |

## 受影響的檔案清單

### Mixin 檔案 (5 個)
- `src/client/java/online/kbpf/dg_lab/mixin/ClientPlayerEntityMixin.java`
- `src/client/java/online/kbpf/dg_lab/mixin/ClientPlayerEntityAccessor.java`
- `src/client/java/online/kbpf/dg_lab/mixin/ClientPlayerInteractionManagerMixin.java`
- `src/client/java/online/kbpf/dg_lab/mixin/LivingEntityAccessor.java`
- `src/client/java/online/kbpf/dg_lab/mixin/tick.java`

### Screen/UI 檔案 (9 個)
- `src/client/java/online/kbpf/dg_lab/client/screen/ConfigScreen.java`
- `src/client/java/online/kbpf/dg_lab/client/screen/WebSocketConfigScreen.java`
- `src/client/java/online/kbpf/dg_lab/client/screen/StrengthScreen/StrengthConfigScreen.java`
- `src/client/java/online/kbpf/dg_lab/client/screen/StrengthScreen/StrengthListWidget.java`
- `src/client/java/online/kbpf/dg_lab/client/screen/WaveformScreen/WaveformConfigScreen.java`
- `src/client/java/online/kbpf/dg_lab/client/screen/WaveformScreen/WaveformListWidget.java`
- `src/client/java/online/kbpf/dg_lab/client/screen/WaveformScreen/Custom/CustomScreen.java`
- `src/client/java/online/kbpf/dg_lab/client/screen/WaveformScreen/Custom/CustomSliderWidget.java`
- `src/client/java/online/kbpf/dg_lab/client/screen/WaveformScreen/Custom/CustomListWidget.java`

### 工具/其他檔案 (4 個)
- `src/client/java/online/kbpf/dg_lab/client/Dg_labClient.java`
- `src/client/java/online/kbpf/dg_lab/client/command/Default.java`
- `src/client/java/online/kbpf/dg_lab/client/createQR/ToolQR.java`
- `src/client/java/online/kbpf/dg_lab/client/webSocketServer/webSocketServer.java`
- `src/client/java/online/kbpf/dg_lab/client/hud/hud.java`

## 遷移步驟

### 步驟 1: 等待 Fabric Loom 1.16 發佈

- ⏳ 當前 Loom 1.16 尚未發佈
- 跟蹤: [Fabric Loom releases](https://github.com/FabricMC/fabric-loom/releases)

### 步驟 2: 更新編譯配置

1. 更新 `gradle.properties`
   ```properties
   minecraft_version=26.1
   loader_version=0.18.4
   fabric_version=0.140.0+26.1
   # 移除 yarn_mappings 行
   ```

2. 更新 `build.gradle`
   ```gradle
   id 'fabric-loom' version '1.16.0' (或 1.16-SNAPSHOT)
   def targetJavaVersion = 25
   // 移除 mappings 依賴
   ```

3. 更新 `gradle/wrapper/gradle-wrapper.properties`
   ```properties
   distributionUrl=...gradle-9.4.0-bin.zip
   ```

### 步驟 3: 遷移 Mixin 類別

1. 更新 Mixin 檔案中的類別名稱和方法簽名
2. 特別注意 `tick.java` 中的 Minecraft/MinecraftClient 引用
3. 驗證方法注入點是否仍然有效

### 步驟 4: 遷移 GUI/Screen 類別

1. 批量替換 `DrawContext` → `GuiGraphics`
2. 批量替換 `Text` → `Component`
3. 更新文本渲染方法調用 (drawTextWithShadow → drawString)
4. 更新字體引用 (client.textRenderer → client.font)

### 步驟 5: 遷移工具/其他類別

1. 更新 KeyBinding → KeyMapping
2. 更新 Identifier → ResourceLocation
3. 更新 InputUtil → InputConstants
4. 清理死代碼和無用 import

### 步驟 6: 測試和編譯

1. 執行 `./gradlew build` 驗證編譯成功
2. 修正任何編譯錯誤 (特別是 Mixin 方法簽名)
3. 啟動客戶端進行功能測試

### 步驟 7: 更新 fabric.mod.json

```json
{
  "depends": {
    "fabricloader": ">=${loader_version}",
    "fabric-api": "*",
    "minecraft": "~${minecraft_version}"
  }
}
```

## 已知問題和注意事項

### 1. Mixin 方法簽名

- `ClientPlayerEntity.updateHealth(float)` 的確切 Mojang 名稱需要驗證
  - 可能是 `LocalPlayer.hurtTo(float)`
  - 可能在不同方法中實現
  
### 2. HUD 渲染方法

- `GuiGraphics.drawString()` 的方法簽名可能需要額外參數 (例如陰影標誌)
- 原 `DrawContext.drawTextWithShadow()` 的功能需要重新映射

### 3. KeyMapping API

- `KeyMapping.Category.createOrGet()` 的確切簽名需要驗證
- 可能需要 `ResourceLocation` 參數而不是字符串

### 4. 官方映射可用性

- IntelliJ IDEA 需要 2025.3+ 以支持 Mojang mappings
- Fabric Docs 提供自動遷移工具，可能有幫助

## 回滾計劃

如果 Minecraft 26.1 遇到主要問題，可以回滾到 1.21.11（最後支持 Yarn 的版本）：

```properties
minecraft_version=1.21.11
yarn_mappings=1.21.11+build.3
loader_version=0.18.1
fabric_version=0.138.4+1.21.11
loom_version=1.14-SNAPSHOT
```

## 相關資源

- [Fabric 為 Minecraft 26.1](https://fabricmc.net/2026/03/14/261.html)
- [Fabric 文檔 - 遷移到 26.1](https://docs.fabricmc.net/develop/porting/)
- [Fabric Loom 發佈](https://github.com/FabricMC/fabric-loom/releases)
- [Fabric API 變更日誌](https://github.com/FabricMC/fabric/blob/1.21.x/CHANGELOG.md)

## 完成檢查表

- [ ] Loom 1.16 發佈到 Maven
- [ ] 編譯配置全部更新
- [ ] 所有 Mixin 類別名稱遷移
- [ ] 所有 GUI 類別名稱遷移  
- [ ] 工具和工具類遷移
- [ ] 編譯成功，無錯誤或警告
- [ ] 客戶端啟動並可正常遊玩
- [ ] WebSocket 和 HUD 功能正常
- [ ] 新建 Pull Request 並通過 CI
- [ ] 發佈版本 1.3.2+ (26.1)
