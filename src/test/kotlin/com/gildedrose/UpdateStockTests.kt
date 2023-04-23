package com.gildedrose

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import java.time.Instant

class UpdateStockTests {
    private val stockList = standardStockList.copy(
        lastModified = Instant.parse("2022-02-09T12:00:00Z")
    )

    @Test
    fun `doesn't update when lastModified is today`() {
        val sameDayAsLastModified = Instant.parse("2022-02-09T23:59:59Z")

        with(Fixture(standardStockList, sameDayAsLastModified)) {
            assertEquals(Status.OK, routes(Request(GET, "/")).status)
            assertEquals(stockList, load())
        }
    }

    @Test
    fun `updates when lastModified was yesterday`() {
        val nextDayAfterLastModified =  Instant.parse("2022-02-10T00:00:00Z")

        with(Fixture(standardStockList, nextDayAfterLastModified)) {
            assertEquals(Status.OK, routes(Request(GET, "/")).status)
            assertNotEquals(stockList, load())
        }
    }
}
