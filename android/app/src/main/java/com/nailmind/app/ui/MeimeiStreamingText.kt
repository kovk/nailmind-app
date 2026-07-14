package com.nailmind.app.ui

internal fun splitMeimeiStreamText(
    text: String,
    maxCodePoints: Int = 2,
): List<String> {
    require(maxCodePoints > 0)
    if (text.isEmpty()) return emptyList()

    val chunks = mutableListOf<String>()
    var start = 0
    while (start < text.length) {
        val remaining = text.codePointCount(start, text.length)
        val count = minOf(maxCodePoints, remaining)
        val end = text.offsetByCodePoints(start, count)
        chunks += text.substring(start, end)
        start = end
    }
    return chunks
}
