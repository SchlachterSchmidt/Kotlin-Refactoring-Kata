package com.gildedrose

import com.gildedrose.com.gildedrose.StockList
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class StockTest {
    private val initialStockList = standardStockList.copy(
        lastModified = Instant.parse("2022-02-09T23:59:59Z")
    )
    private val fixture = Fixture(initialStockList)
    private val stock = Stock(fixture.stockFile, ZoneId.of("Europe/London"))

    @Test
    fun `loads stock from file`() {
        val now = Instant.parse("2022-02-09T23:59:59Z")
        assertThat(stock.stockList(now)).isEqualTo(initialStockList)
    }

    @Test
    fun `updates stock if lastModified yesterday`() {
        val now = Instant.parse("2022-02-10T00:00:00Z")
        val expectedUpdatedResult = initialStockList.copy(lastModified = now)
        assertThat(stock.stockList(now)).isEqualTo(expectedUpdatedResult)
        assertThat(fixture.load()).isEqualTo(expectedUpdatedResult)
    }

}


class Stock(
    private val stockFile: File,
    private val zone: ZoneId
) {
    fun stockList(now: Instant): StockList {
        val loaded = stockFile.loadItems()
        return if (loaded.lastModified.daysTo(now, zone) == 0L)
            loaded
        else
            loaded.copy(lastModified = now).also { it.saveTo(stockFile) }
    }
}

// in the original video lesson there is a whole explanation and tests that are not really relevant for me
private fun Instant.daysTo(that: Instant, zone: ZoneId): Long =
    LocalDate.ofInstant(that, zone).toEpochDay() - LocalDate.ofInstant(this, zone).toEpochDay()

