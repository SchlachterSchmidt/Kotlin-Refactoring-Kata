package com.gildedrose

import com.gildedrose.com.gildedrose.Stock
import com.gildedrose.com.gildedrose.update
import org.http4k.core.Method.GET
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.io.File
import java.time.Instant
import java.time.ZoneId

fun main() {
    val file = File("stock.tsv")
    val server = Server(routesFor(file) { Instant.now() } )
    server.start()
}

fun routesFor(
    stockFile: File,
    clock: () -> Instant,
): RoutingHttpHandler {

    val londonZoneId = ZoneId.of("Europe/London")

    val stock = Stock(
        stockFile = stockFile,
        zoneId = londonZoneId,
        update = ::update
    )
    return routes(
        "/" bind GET to listHandler(clock = clock, zoneId = londonZoneId, listing = stock::stockList)
    )
}
