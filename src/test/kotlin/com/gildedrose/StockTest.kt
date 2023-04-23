package com.gildedrose

import com.gildedrose.com.gildedrose.Stock
import com.gildedrose.com.gildedrose.StockList
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.ZoneId
import java.util.concurrent.Callable
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Executors

class StockTest {
    private val initialStockList = StockList(
        lastModified = Instant.parse("2022-02-09T23:59:59Z"),
        items = listOf(
            Item("banana", oct29.minusDays(1), 42u),
            Item("kumquat", oct29.plusDays(1), 101u)
        )
    )

    private val fixture = Fixture(
        initialStockList = initialStockList,
        now = initialStockList.lastModified
    )

    private val stock = Stock(fixture.stockFile, ZoneId.of("Europe/London"), ::update)

    @Test
    fun `loads stock from file`() {
        val now = Instant.parse("2022-02-09T23:59:59Z")
        assertThat(stock.stockList(now)).isEqualTo(initialStockList)
    }

    @Test
    fun `updates stock if lastModified yesterday`() {
        val now = Instant.parse("2022-02-10T00:00:00Z")
        val expectedUpdatedResult = StockList(
            lastModified = now,
            items = listOf(
                Item("banana", oct29.minusDays(1), 41u),
                Item("kumquat", oct29.plusDays(1), 100u)
            )
        )

        assertThat(stock.stockList(now)).isEqualTo(expectedUpdatedResult)
        assertThat(fixture.load()).isEqualTo(expectedUpdatedResult)
    }

    @Test
    fun `updates stock by two days if lastModified two days ago`() {
        val now = Instant.parse("2022-02-11T00:00:00Z")
        val expectedUpdatedResult = StockList(
            lastModified = now,
            items = listOf(
                Item("banana", oct29.minusDays(1), 40u),
                Item("kumquat", oct29.plusDays(1), 99u)
            )
        )

        assertThat(stock.stockList(now)).isEqualTo(expectedUpdatedResult)
        assertThat(fixture.load()).isEqualTo(expectedUpdatedResult)
    }

    @Test
    fun `does not update if lastModified is tomorrow`() {
        val now = Instant.parse("2022-02-08T00:00:00Z")

        assertThat(stock.stockList(now)).isEqualTo(initialStockList)
        assertThat(fixture.load()).isEqualTo(initialStockList)
    }

    @Test
    fun `parallel execution`() {
        val count = 8
        val executor = Executors.newFixedThreadPool(count)
        val barrier = CyclicBarrier(count)
        val futures = executor.invokeAll(
            (1..count).map {
                Callable() {
                    barrier.await()
                    `updates stock if lastModified yesterday`()
                }
            }
        )
        futures.forEach { it.get() }
    }

    @Test
    fun `sanity check`() {
        for(i in 1..10) {
            `parallel execution`()
        }
    }

}

