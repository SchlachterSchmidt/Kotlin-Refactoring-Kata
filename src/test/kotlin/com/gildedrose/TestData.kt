package com.gildedrose

import com.gildedrose.com.gildedrose.StockList
import java.time.Instant
import java.time.LocalDate

val now = LocalDate.parse("2023-04-05")

fun emptyStockList() = StockList(
    Instant.now(),
    listOf()
)

fun standardStockList() = StockList(
    Instant.now(),
    listOf(
        Item("banana", now.minusDays(1), 42u),
        Item("kumquat", now.plusDays(1), 101u)
    )
)
