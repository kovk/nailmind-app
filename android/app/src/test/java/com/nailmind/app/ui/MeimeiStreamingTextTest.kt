package com.nailmind.app.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class MeimeiStreamingTextTest {
    @Test
    fun `long stream delta is split into visible increments`() {
        assertEquals(
            listOf("我帮", "你挑", "了几", "款"),
            splitMeimeiStreamText("我帮你挑了几款", maxCodePoints = 2),
        )
    }

    @Test
    fun `stream splitting keeps emoji intact`() {
        val text = "好呀🌸看看"

        assertEquals(text, splitMeimeiStreamText(text, maxCodePoints = 2).joinToString(""))
    }
}
