package com.gildedrose

import com.gildedrose.com.gildedrose.StockList
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.testing.ApprovalTest
import org.http4k.testing.Approver
import org.http4k.testing.assertApproved
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.time.Instant
import java.time.LocalDate

@ExtendWith(ApprovalTest::class)
class ListStockTests {

    private val now = LocalDate.parse("2023-04-05")

    @TempDir
    lateinit var dir: File
    private val stockFile by lazy { dir.resolve("stock.tsv") }

    @Test
    fun `print non-empty stock list`(approver: Approver) {

        StockList(
            Instant.now(),
            listOf(
                Item("banana", now.minusDays(1), 42u),
                Item("kumquat", now.plusDays(1), 101u)
            )
        ).saveTo(stockFile)
        val routes = routesFor(stockFile) { now }

        val response = routes(Request(GET, "/"))

        approver.assertApproved(response, OK)
    }
}
