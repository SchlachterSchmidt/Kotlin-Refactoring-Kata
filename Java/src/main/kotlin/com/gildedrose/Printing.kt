package com.gildedrose

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle.LONG
import java.time.temporal.ChronoUnit.DAYS

fun List<Item>.printout(date: LocalDate): List<String> =
    listOf(dateFormatter.format(date)) + this.map {
        it.toPrintout(date)
    }

fun Item.toPrintout(date: LocalDate) =
    "$name, ${daysUntilSellBy(date)}, $quality"

fun Item.daysUntilSellBy(date: LocalDate): Long =
    DAYS.between(date, this.sellByDate)

private val dateFormatter = DateTimeFormatter.ofLocalizedDate(LONG)
