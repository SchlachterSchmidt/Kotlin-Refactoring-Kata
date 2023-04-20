package com.gildedrose

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.testing.ApprovalTest
import org.http4k.testing.Approver
import org.http4k.testing.assertApproved
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import java.io.File

@ExtendWith(ApprovalTest::class)
class ListStockTests {
    @TempDir lateinit var dir: File
    private val stockFile by lazy { dir.resolve("stock.tsv") }
    private val routes by lazy { routesFor(stockFile) { now } }

    @Test
    fun `print non-empty stock list`(approver: Approver) {
        standardStockList().saveTo(stockFile)

        val response = routes(Request(GET, "/"))

        approver.assertApproved(response, OK)
    }

    @Test
    fun `list stock sees file updates`(approver: Approver) {
        standardStockList().saveTo(stockFile)
        assertEquals(OK, routes(Request(GET, "/")).status)

        emptyStockList().saveTo(stockFile)

        val response = routes(Request(GET, "/"))

        approver.assertApproved(response, OK)
    }
}
