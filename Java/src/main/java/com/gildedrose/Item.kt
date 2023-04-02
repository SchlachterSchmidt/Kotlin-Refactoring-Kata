package com.gildedrose

open class Item(
    val name: String,
    var sellIn: Int = 0,
    var quality: Int = 0,
) {
    override fun toString() = "$name, $sellIn, $quality"
}

class BaseItem(
    name: String,
    sellIn: Int,
    quality: Int,
    private val aging: () -> Int = { 1 },
    private val degradation: (Int, Int) -> Int = { sellIn: Int, _: Int ->
        when {
            sellIn < 0 -> 2
            else -> 1
        }
    },
    private val saturation: (Int) -> Int = fun(quality: Int) = when {
        quality < 0 -> 0
        quality > 50 -> 50
        else -> quality
    },
) : Item(
    name,
    sellIn,
    quality
) {
    fun update() {
        sellIn -= aging()
        quality = saturation(quality - degradation(sellIn, quality))
    }
}

fun Sulfuras(name: String, sellIn: Int, quality: Int) = BaseItem(
    name,
    sellIn,
    quality,
    aging = { 0 },
    degradation = { _, _ -> 0 },
    saturation = { it }
)

fun Brie(name: String, sellIn: Int, quality: Int) = BaseItem(
    name,
    sellIn,
    quality,
    degradation = { sellIn, _ ->
        when {
            sellIn < 0 -> -2
            else -> -1
        }
    }
)

fun Pass(name: String, sellIn: Int, quality: Int) = BaseItem(
    name,
    sellIn,
    quality,
    degradation = { sellIn, quality ->
        when {
            sellIn < 0 -> quality
            sellIn < 5 -> -3
            sellIn < 10 -> -2
            else -> -1
        }
    }
)
