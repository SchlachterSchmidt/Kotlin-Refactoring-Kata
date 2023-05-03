package com.gildedrose

import com.gildedrose.com.gildedrose.Stock
import com.gildedrose.com.gildedrose.update
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.then
import org.http4k.filter.ServerFilters.CatchAll
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
    return catchAllFilter.then(
        routes(
            "/" bind GET to listHandler(clock = clock, zoneId = londonZoneId, listing = stock::stockList),
            "/error" bind GET to { error("deliberate") }
        )
    )
}

val log: Logger = LoggerFactory.getLogger("Uncaught Exceptions")
val catchAllFilter = CatchAll {
    log.error("uncaught exception", it)
    Response(INTERNAL_SERVER_ERROR).body("Something went wrong")
}
