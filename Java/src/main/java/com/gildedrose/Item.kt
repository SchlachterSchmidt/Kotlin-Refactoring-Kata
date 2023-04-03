package com.gildedrose


data class Item(
    private val name: String,
    private val sellIn: Int = 0,
    private val quality: Int = 0,
    private val aging: () -> Int = Aging.standard,
    private val degradation: (Int, Int) -> Int = Degradation.standard,
    private val saturation: (Int) -> Int = Saturation.standard,
) {
    override fun toString() = "$name, $sellIn, $quality"

    fun updated(): Item {
        val sellIn = sellIn - aging()
        return this.copy(
            sellIn = sellIn,
            quality = saturation(this.quality - degradation(sellIn, this.quality))
        )
    }
}
