package com.gildedrose

import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle.LONG
import java.time.temporal.ChronoUnit.DAYS

private val handlebars = HandlebarsTemplates().HotReload("src/main/kotlin")

fun routes(
    stock: List<Item>,
    clock: () -> LocalDate = LocalDate::now
) = org.http4k.routing.routes(
    "/" bind GET to {
        val now = clock()
        Response(Status.OK).body(
            handlebars(
                StockListViewModel(
                    now = dateFormatter.format(now),
                    items = stock.map { item ->
                        item.toMap(now)
                    }
                )
            )
        )
    }
)

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
