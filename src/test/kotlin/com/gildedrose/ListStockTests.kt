package com.gildedrose

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.testing.ApprovalTest
import org.http4k.testing.Approver
import org.http4k.testing.assertApproved
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(ApprovalTest::class)
class ListStockTests {

    private val now = LocalDate.parse("2023-04-05")

    @Test
    fun `print non-empty stock list`(approver: Approver) {
        val stock = listOf(
            Item("banana", now.minusDays(1), 42u),
            Item("kumquat", now.plusDays(1), 101u)
        )

        val server = Server(stock) { now }
        val client = server.routes

        val response = client(Request(GET, "/"))
        approver.assertApproved(response, OK)
    }
}
