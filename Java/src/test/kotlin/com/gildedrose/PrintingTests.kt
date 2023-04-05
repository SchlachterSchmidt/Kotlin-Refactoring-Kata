package com.gildedrose

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PrintingTests {

    private val now = LocalDate.parse("2023-04-05")

    @Test
    fun `print empty stock list`() {
        val stock = listOf<Item>()

        val expected = listOf("April 5, 2023")

        Assertions.assertThat(stock.printout(LocalDate.now())).isEqualTo(expected)
    }


    @Test
    fun `print non-empty stock list`() {
        val stock = listOf(
            Item("banana", now.minusDays(1), 42u),
            Item("kumquat", now.plusDays(1), 101u)
        )

        val expected = listOf(
            "April 5, 2023",
            "banana, -1, 42",
            "kumquat, 1, 101"
        )

        Assertions.assertThat(stock.printout(now)).isEqualTo(expected)
    }
}
