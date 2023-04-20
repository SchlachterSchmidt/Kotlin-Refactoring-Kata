package com.gildedrose

import org.http4k.routing.RoutingHttpHandler
import java.io.File
import java.time.LocalDate

fun main() {
    val file = File("stock.tsv")
    val server = Server(routesFor(file))
    server.start()
}

fun routesFor(stockFile: File, clock: () -> LocalDate = LocalDate::now): RoutingHttpHandler {
    val stock = stockFile.loadItems()

    return routes(stock, clock)
}
