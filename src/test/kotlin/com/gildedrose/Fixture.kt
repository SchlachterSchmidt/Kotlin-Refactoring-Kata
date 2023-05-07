package com.gildedrose

import com.gildedrose.com.gildedrose.Analytics
import com.gildedrose.com.gildedrose.StockList
import java.io.File
import java.nio.file.Files
import java.time.Instant

class Fixture(
    initialStockList: StockList,
    private val now: Instant,
    val events: MutableList<Any> = mutableListOf(),
    val stockFile: File = Files.createTempFile("stock", ".tsv").toFile(),
) {
    init {
        save(initialStockList)
    }

    val routes = routesFor(
        stockFile = stockFile,
        clock = { now },
        analytics = analytics then { events.add(it) }
    )

    fun save(stockList: StockList) {
        stockList.saveTo(stockFile)
    }

    fun load(): StockList = stockFile.loadItems()
}

private infix fun Analytics.then(that: Analytics): Analytics = { event ->
    this(event)
    that(event)
}
