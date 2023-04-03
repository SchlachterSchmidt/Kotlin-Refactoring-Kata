package com.gildedrose


class Item(
    val name: String,
    var sellIn: Int = 0,
    var quality: Int = 0,
    private val aging:() -> Int = Aging.standard,
    private val degradation: (Int, Int) -> Int = Degradation.standard,
    private val saturation: (Int) -> Int = Saturation.standard
) {
    override fun toString() = "$name, $sellIn, $quality"
    fun update() {
        sellIn -= aging()
        quality = saturation(quality - degradation(sellIn, quality))
    }

    fun updated(): Item {
        val sellIn = sellIn - aging()
        val quality = saturation(quality - degradation(sellIn, quality))
        return Item(
            name,
            sellIn,
            quality,
            aging,
            degradation,
            saturation
        )
    }
}
