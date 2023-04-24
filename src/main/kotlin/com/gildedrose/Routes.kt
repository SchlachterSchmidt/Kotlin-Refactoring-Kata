package com.gildedrose

import com.gildedrose.com.gildedrose.StockList
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle.LONG
import java.time.temporal.ChronoUnit.DAYS

private val handlebars = HandlebarsTemplates().HotReload("src/main/kotlin")

fun listHandler(
    clock: () -> Instant,
    zoneId: ZoneId,
    listing: (Instant) -> StockList,
): HttpHandler = {
    val now = clock()
    val today: LocalDate = LocalDate.ofInstant(now, zoneId)
    val stockList = listing(now)

    Response(OK).body(
        handlebars(
            StockListViewModel(
                now = dateFormatter.format(today),
                items = stockList.map { item ->
                    item.toMap(today)
                }
            )
        )
    )
}

fun Item.daysUntilSellBy(date: LocalDate): Long =
    DAYS.between(date, this.sellByDate)

val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(LONG)

data class StockListViewModel(
    val now: String,
    val items: List<Map<String,String>>
) : ViewModel

private fun Item.toMap(now: LocalDate): Map<String, String> = mapOf(
    "name" to name,
    "sellByDate" to dateFormatter.format(sellByDate),
    "sellByDays" to daysUntilSellBy(now).toString(),
    "quality" to quality.toString()
)
