package com.gildedrose

import com.gildedrose.com.gildedrose.StockList
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.IOException
import java.time.Instant
import java.time.LocalDate

class PersistenceTests {

    private val sellByDate = LocalDate.parse("2023-04-06")
    private val now = Instant.now()

    private val stock: List<Item> = listOf(
        Item("Banana", sellByDate, 42u),
        Item("Kumquat", sellByDate.plusDays(1), 101u),
    )


    @Test
    fun `load and save empty stock list`() {
        val stockList = StockList(
            now, emptyList()
        )

        assertEquals(stockList, stockList.toLines().toStockList())
    }

    @Test
    fun `load empty  file`() {
        assertEquals(
            StockList(Instant.EPOCH , emptyList()),
            emptySequence<String>().toStockList()
        )
    }

    @Test
    fun `load with no LastModified header`() {
        val lines = sequenceOf("# Banana")
        val stockList = StockList(
            Instant.EPOCH, emptyList()
        )

        assertEquals(stockList, lines.toStockList())
    }

    @Test
    fun `load with blank LastModified header`() {
        val lines = sequenceOf("# LastModified:")

        assertThatThrownBy {
            lines.toStockList()
        }
            .isInstanceOf(IOException::class.java)
            .hasMessage("Could not parse LastModified header: Text '' could not be parsed at index 0")
    }

    @Test
    fun `load and save`(@TempDir dir: File) {
        val file = File(dir, "stock.tsv")

        val stockList = StockList(now, stock)
        stockList.saveTo(file)

        assertEquals(stockList, file.loadItems())
    }
}
