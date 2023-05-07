package com.gildedrose

import com.gildedrose.com.gildedrose.Analytics
import com.gildedrose.com.gildedrose.AnalyticsEvent
import com.gildedrose.com.gildedrose.LoggingAnalytics
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
import java.io.File
import java.time.Instant
import java.time.ZoneId

fun main() {
    val file = File("stock.tsv")
    val server = Server(routesFor(
        stockFile = file,
        clock = { Instant.now() },
        analytics = analytics
    ))
    server.start()
}

val analytics = LoggingAnalytics(::println)


fun routesFor(
    stockFile: File,
    clock: () -> Instant,
    analytics: Analytics
): RoutingHttpHandler {

    val londonZoneId = ZoneId.of("Europe/London")

    val stock = Stock(
        stockFile = stockFile,
        zoneId = londonZoneId,
        update = ::update
    )
    return catchAll(analytics).then(
        routes(
            "/" bind GET to listHandler(clock = clock, zoneId = londonZoneId, listing = stock::stockList),
            "/error" bind GET to { error("deliberate") }
        )
    )
}

private fun catchAll(analytics: Analytics) = CatchAll {
    analytics(UncaughtExceptionEvent(it))
    Response(INTERNAL_SERVER_ERROR).body("Something went wrong")
}

data class UncaughtExceptionEvent(
    val message: String,
    val stackTrace: List<String>,
) : AnalyticsEvent {
    constructor(exception: Throwable) : this(
        exception.message.orEmpty(),
        exception.stackTrace.map { it.toString() }
    )
}
