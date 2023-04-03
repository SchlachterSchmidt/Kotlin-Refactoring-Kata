package com.gildedrose

class GildedRose(val items: List<Item>) {
    fun updated(): GildedRose = GildedRose(items.map { it.updated() })
}
