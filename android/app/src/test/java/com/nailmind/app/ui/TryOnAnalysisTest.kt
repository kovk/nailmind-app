package com.nailmind.app.ui

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
                    "nailBed" to "甲床偏短"
                )
            )
        )

        assertTrue(analysis.colorHarmony.contains("冷暖反差"))
        assertTrue(analysis.colorHarmony.contains("协调度一般"))
        assertTrue(analysis.shapeAndLength.contains("手指修长"))
    }
}
