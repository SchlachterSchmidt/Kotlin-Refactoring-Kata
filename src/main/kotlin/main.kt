package com.gildedrose

import com.gildedrose.com.gildedrose.Stock
import org.http4k.routing.RoutingHttpHandler
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun main() {
    val file = File("stock.tsv")
    val server = Server(routesFor(file) { Instant.now() } )
    server.start()
}

fun routesFor(
    stockFile: File,
    calendar: () -> LocalDate = LocalDate::now,
    clock: () -> Instant,
): RoutingHttpHandler {
    val stock = Stock(
        stockFile = stockFile,
        zoneId = ZoneId.of("Europe/London"),
        update = ::update
    )
    return routes(
        stock = {stock.stockList(clock()) },
        calendar = calendar
    )
}

fun update(items: List<Item>, days: Int) = items.map { item -> item.copy(quality = item.quality - days.toUInt()) }

