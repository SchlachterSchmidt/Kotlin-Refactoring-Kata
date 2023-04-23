package com.gildedrose

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate

class UpdateStockTests {
    private val stockList = standardStockList.copy(
        lastModified = Instant.parse("2022-02-09T12:00:00Z")
    )

    @Test
    fun `doesn't update when lastModified is today`() {
        val sameDayAsLastModified = LocalDate.parse("2022-02-09")

        with(Fixture(standardStockList, sameDayAsLastModified)) {
            assertEquals(Status.OK, routes(Request(GET, "/")).status)
            assertEquals(stockList, load())
        }
    }

    @Disabled("WIP") @Test
    fun `updates when lastModified was yesterday`() {
        val nextDayAfterLastModified = LocalDate.parse("2022-02-10")

        with(Fixture(standardStockList, nextDayAfterLastModified)) {
            assertEquals(Status.OK, routes(Request(GET, "/")).status)
            assertNotEquals(stockList, load())
        }
    }
}
