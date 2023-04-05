package com.gildedrose

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate


class Tests {

    @Test
    fun `should do stuff`() {
        val stock = listOf<Item>()
        val newStock = stock + Item("banana", LocalDate.now(), 42u)

        assertThat(listOf(Item("banana", LocalDate.now(), 42u))).isEqualTo(newStock)
    }

}

