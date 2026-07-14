package com.nailmind.app.ui

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StyleFilterMatcherTest {
    private fun selection(tab: StyleBrowseTab, label: String) = StyleFilterSelection(
        tab = tab,
        option = StyleBrowseOption(label, listOf(label))
    )

    @Test
    fun sameCategoryUsesOrMatching() {
        val selected = setOf(selection(StyleBrowseTab.Vibe, "韩系"), selection(StyleBrowseTab.Vibe, "甜美"))

        val matches = matchesStyleFilterSelections(selected) { _, option -> option.label == "甜美" }

        assertTrue(matches)
    }

    @Test
    fun differentCategoriesUseAndMatching() {
        val selected = setOf(
            selection(StyleBrowseTab.Vibe, "韩系"),
            selection(StyleBrowseTab.Effect, "猫眼"),
            selection(StyleBrowseTab.NailShape, "短方")
        )

        val missingCatEye = matchesStyleFilterSelections(selected) { _, option -> option.label != "猫眼" }
        val matchesAll = matchesStyleFilterSelections(selected) { _, _ -> true }

        assertFalse(missingCatEye)
        assertTrue(matchesAll)
    }

    @Test
    fun emptySelectionShowsAllStyles() {
        assertTrue(matchesStyleFilterSelections(emptySet()) { _, _ -> false })
    }
}
