package com.gildedrose

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate


class StockTest {

    private val sellBy = LocalDate.parse("2023-04-05")

    @Test
    fun `add item to stock`() {
        val stock = listOf<Item>()

        val newStock = stock + Item("banana", sellBy, 42u)

        assertThat(listOf(Item("banana", sellBy, 42u))).isEqualTo(newStock)
    }
}
