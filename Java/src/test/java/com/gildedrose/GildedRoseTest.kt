package com.gildedrose

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class GildedRoseTest {
    @Test
    fun foo() {
        val items = arrayOf<Item>(BaseItem("foo", 0, 0))
        val app = GildedRose(items)
        app.updateQuality()
        Assertions.assertEquals("foo", app.items[0].name)
    }
}
