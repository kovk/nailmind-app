package com.nailmind.app.ui

import com.nailmind.app.data.api.TryOnHistoryItemDto
import org.junit.Assert.assertTrue
import org.junit.Test

class TryOnAnalysisTest {
    @Test
    fun `analysis uses detected hand traits and selected nail settings`() {
        val analysis = buildTryOnAnalysis(
            styleName = "雾蓝猫眼",
            nailType = "中椭圆",
            skinTone = "暖黄皮",
            tags = listOf("通勤", "猫眼"),
            context = tryOnAnalysisContext(
                selectedLength = "natural_short",
                selectedShape = "squoval",
                detectedTraits = mapOf("skinTone" to "暖黄皮", "handShape" to "手掌偏宽")
            )
        )

        assertTrue(analysis.colorHarmony.contains("暖黄皮"))
        assertTrue(analysis.shapeAndLength.contains("手掌偏宽"))
        assertTrue(analysis.shapeAndLength.contains("自然短款"))
        assertTrue(analysis.outfitAdvice.contains("通勤"))
    }

    @Test
    fun `analysis remains useful when vision traits are unavailable`() {
        val analysis = buildTryOnAnalysis(
            styleName = "裸粉法式",
            nailType = "短圆",
            skinTone = "通用",
            tags = listOf("日常", "法式"),
            context = tryOnAnalysisContext(selectedLength = "", selectedShape = "")
        )

        assertTrue(analysis.colorHarmony.isNotBlank())
        assertTrue(analysis.shapeAndLength.contains("短圆"))
        assertTrue(analysis.outfitAdvice.contains("日常"))
    }

    @Test
    fun `analysis reports skin and hand shape mismatches`() {
        val analysis = buildTryOnAnalysis(
            styleName = "冷白银灰",
            nailType = "短方",
            skinTone = "冷白皮",
            tags = listOf("镜面"),
            context = tryOnAnalysisContext(
                selectedLength = "natural_short",
                selectedShape = "square",
                detectedTraits = mapOf("skinTone" to "暖黄皮", "handShape" to "手掌偏宽")
            )
        )

        assertTrue(analysis.colorHarmony.contains("协调度一般"))
        assertTrue(analysis.shapeAndLength.contains("适配度一般"))
    }

    @Test
    fun `undertone matching does not confuse skin depth with temperature`() {
        val coolAnalysis = buildTryOnAnalysis(
            styleName = "冷调雾蓝",
            nailType = "短圆",
            skinTone = "冷白皮",
            tags = listOf("日常"),
            context = tryOnAnalysisContext(
                selectedLength = "natural_short",
                selectedShape = "round",
                detectedTraits = mapOf("skinTone" to "自然偏冷")
            )
        )
        val deepWarmAnalysis = buildTryOnAnalysis(
            styleName = "暖棕",
            nailType = "短圆",
            skinTone = "暖黄皮",
            tags = listOf("日常"),
            context = tryOnAnalysisContext(
                selectedLength = "natural_short",
                selectedShape = "round",
                detectedTraits = mapOf("skinTone" to "深暖肤色")
            )
        )

        assertTrue(coolAnalysis.colorHarmony.contains("整体协调"))
        assertTrue(deepWarmAnalysis.colorHarmony.contains("整体协调"))
    }

    @Test
    fun `style color is assessed when catalog skin label is missing`() {
        val analysis = buildTryOnAnalysis(
            styleName = "蓝色星星美甲",
            nailType = "短圆",
            skinTone = "",
            tags = listOf("蓝色", "日常"),
            context = tryOnAnalysisContext(
                selectedLength = "natural_short",
                selectedShape = "round",
                detectedTraits = mapOf(
                    "skinTone" to "自然肤色",
                    "skinUndertone" to "暖调",
                    "handShape" to "手指修长",
                    "nailBed" to "甲床偏短",
                    "colorHarmonyVerdict" to "协调度一般",
                    "colorHarmonyReason" to "暖调肤色与蓝色款式形成冷暖反差。",
                    "colorHarmonySuggestion" to "降低蓝色饱和度会更柔和。"
                )
            )
        )

        assertTrue(analysis.colorHarmony.contains("大模型判断"))
        assertTrue(analysis.colorHarmony.contains("冷暖反差"))
        assertTrue(analysis.colorHarmony.contains("协调度一般"))
        assertTrue(analysis.shapeAndLength.contains("手指修长"))
    }

    @Test
    fun `history result restores detected traits for fit analysis`() {
        val context = tryOnHistoryAnalysisContext(
            TryOnHistoryItemDto(
                id = "tryon-record-0001",
                jobId = "tryon-0001",
                resultUrl = "/files/results/tryon-0001-gpt-image.png",
                durationMs = 1_200,
                styleName = "蓝色星星美甲",
                styleId = "style-001",
                source = "gpt-image-live",
                selectedLength = "natural_short",
                selectedShape = "squoval",
                detectedTraits = mapOf(
                    "skinTone" to "自然偏白",
                    "skinUndertone" to "暖黄调",
                    "handShape" to "修长骨感型",
                    "nailBed" to "甲床窄长",
                    "colorHarmonyVerdict" to "较协调",
                    "colorHarmonyReason" to "暖黄调肤色与红色主色相互提亮。"
                ),
                createdAt = "2026-07-15T04:21:18"
            )
        )

        assertTrue(context.traits.skinTone.contains("自然偏白"))
        assertTrue(context.traits.handShape.contains("修长骨感型"))
        assertTrue(context.traits.nailBed.contains("甲床窄长"))
        assertTrue(context.traits.colorHarmonyReason.contains("红色主色"))
    }

    @Test
    fun `history result remains safe when vision traits are null`() {
        val context = tryOnHistoryAnalysisContext(
            TryOnHistoryItemDto(
                id = "tryon-record-0002",
                resultUrl = "/files/results/legacy.png",
                durationMs = 800,
                styleName = "旧试戴记录",
                styleId = "style-002",
                source = "legacy",
                selectedLength = "natural_short",
                selectedShape = "round",
                detectedTraits = null,
                createdAt = "2026-07-15T04:22:00"
            )
        )

        assertTrue(context.traits.skinTone.isBlank())
        assertTrue(context.traits.colorHarmonyReason.isBlank())
    }
}
