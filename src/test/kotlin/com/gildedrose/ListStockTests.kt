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

@ExtendWith(ApprovalTest::class)
class ListStockTests {

    @Test
    fun `print non-empty stock list`(approver: Approver) {
        with(Fixture(standardStockList)) {

            val response = routes(Request(GET, "/"))

            approver.assertApproved(response, OK)
        }
    }

    @Test
    fun `list stock sees file updates`(approver: Approver) {
        with(Fixture(standardStockList)) {
            assertEquals(OK, routes(Request(GET, "/")).status)

            save(emptyStockList)

            val response = routes(Request(GET, "/"))
            approver.assertApproved(response, OK)
        }

    }
}
