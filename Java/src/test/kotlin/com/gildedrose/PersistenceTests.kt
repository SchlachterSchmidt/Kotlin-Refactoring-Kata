package com.gildedrose

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.time.LocalDate

class PersistenceTests {

    private val sellByDate = LocalDate.parse("2023-04-06")

    @Test
    fun `load and save empty file`(@TempDir dir: File) {
        val file = File(dir, "stock.tsv")
        val stock = listOf<Item>()

        stock.saveTo(file)
        assertThat(file.loadItems()).isEqualTo(stock)
    }

    @Test
    fun `load and save`(@TempDir dir: File) {
        val file = File(dir, "stock.tsv")
        val stock = listOf(
            Item("Banana", sellByDate, 42u),
            Item("Kumquat", sellByDate.plusDays(1), 101u),
        )

        stock.saveTo(file)
        assertThat(file.loadItems()).isEqualTo(stock)
    }
}
