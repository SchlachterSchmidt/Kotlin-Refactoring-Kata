package com.gildedrose

import com.gildedrose.com.gildedrose.StockList
import java.io.File
import java.nio.file.Files
import java.time.Instant

class Fixture(
    initialStockList: StockList,
    private val now: Instant,
    val stockFile: File = Files.createTempFile("stock", ".tsv").toFile(),
) {
    init {
        save(initialStockList)
    }

    val routes = routesFor(
        stockFile = stockFile,
        clock = { now }
    )

    fun save(stockList: StockList) {
        stockList.saveTo(stockFile)
    }

    fun load(): StockList = stockFile.loadItems()
}
