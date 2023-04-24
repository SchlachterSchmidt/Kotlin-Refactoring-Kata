package com.gildedrose

import com.gildedrose.com.gildedrose.StockList
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.testing.ApprovalTest
import org.http4k.testing.Approver
import org.http4k.testing.assertApproved
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Instant

@ExtendWith(ApprovalTest::class)
class ListStockTests {

    private val initialStockList = StockList(
        lastModified = Instant.parse("2022-02-09T12:00:00Z"),
        items = listOf(
            Item("banana", oct29.minusDays(1), 42u),
            Item("kumquat", oct29.plusDays(1), 101u)
        )
    )

    @Test
    fun `print non-empty stock list`(approver: Approver) {

        with(
            Fixture(initialStockList, Instant.parse("2021-10-29T12:00:00Z"))
        ) {
            val response = routes(Request(GET, "/"))

            approver.assertApproved(response, OK)
        }
    }

    @Test
    fun `list stock sees file updates`(approver: Approver) {
        with(
            Fixture(initialStockList, Instant.parse("2021-10-29T12:00:00Z"))
        ) {
            assertEquals(OK, routes(Request(GET, "/")).status)

            val emptyStockList = StockList(
                lastModified = Instant.parse("2022-02-09T12:00:00Z"),
                items = listOf()
            )
            save(emptyStockList)

            val response = routes(Request(GET, "/"))
            approver.assertApproved(response, OK)
        }
    }

    @Test
    fun `doesn't update when lastModified is today`(approver: Approver) {
        val sameDayAsLastModified = Instant.parse("2022-02-09T23:59:59Z")

        with(
            Fixture(initialStockList, sameDayAsLastModified)
        ) {
            approver.assertApproved(routes(Request(GET, "/")), OK)
            assertEquals(initialStockList, load())
        }
    }

    @Test
    fun `updates when lastModified was yesterday`(approver: Approver) {
        val nextDayAfterLastModified = Instant.parse("2022-02-10T00:00:00Z")

        with(
            Fixture(initialStockList, nextDayAfterLastModified)
        ) {
            approver.assertApproved(routes(Request(GET, "/")), OK)
            Assertions.assertNotEquals(initialStockList, load())
        }
    }
}
