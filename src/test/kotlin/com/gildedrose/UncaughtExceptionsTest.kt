package com.gildedrose

import com.gildedrose.com.gildedrose.StockList
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.junit.jupiter.api.Test
import java.time.Instant

class UncaughtExceptionsTest {

    private val stockList = StockList(
        lastModified = Instant.parse("2022-02-09T12:00:00Z"),
        items = emptyList()
    )

    @Test
    fun `uncaught exceptions`() {
        with(
            Fixture(stockList, now = Instant.now())
        ) {
            assertThat(events.size).isEqualTo(0)
            val response = routes(Request(GET, "/error"))
            assertThat(response.body.toString())
                .isEqualTo("Something went wrong")

            assertThat(events.size).isEqualTo(1)
            assertThat(events.first()::class).isEqualTo(UncaughtExceptionEvent::class)
        }
    }

}
