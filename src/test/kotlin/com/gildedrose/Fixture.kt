package com.gildedrose

import com.gildedrose.com.gildedrose.StockList
import java.io.File
import java.nio.file.Files
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class Fixture(
    initialStockList: StockList,
    private val now: Instant,
    val stockFile: File = Files.createTempFile("stock", ".tsv").toFile(),
) {

    private val today: LocalDate = LocalDate.ofInstant(now, ZoneId.of("Europe/London"))

    init {
        save(initialStockList)
    }

    val routes = routesFor(
        stockFile = stockFile,
        calendar = { today },
        clock = { now }
    )

    fun save(stockList: StockList) {
        stockList.saveTo(stockFile)
    }

    fun load(): StockList = stockFile.loadItems()
}
