package com.gildedrose

import com.gildedrose.com.gildedrose.update
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test

class UpdatingTests {

    private val items = listOf(Item("Banana", oct29, 42))

    @Test
    fun `items decrease in quality one per day`() {
        assertThat(update(items, days = 1))
            .isEqualTo(listOf(Item("Banana", oct29, 41)))
    }

    @Test
    fun `items decrease in quality by two per two day`() {
        assertThat(update(items, days = 2))
            .isEqualTo(listOf(Item("Banana", oct29, 40)))
    }

    @Test
    fun `items decrease in quality by 0 for same day`() {
        assertThat(update(items, days = 0))
            .isEqualTo(listOf(Item("Banana", oct29, 42)))
    }

    @Test
    fun `quality doesnt decrease past zero `() {
        assertThat(update(listOf(Item("Banana", oct29, 0)), days = 1))
            .isEqualTo(listOf(Item("Banana", oct29, 0)))

        assertThat(update(listOf(Item("Banana", oct29, 1)), days = 2))
            .isEqualTo(listOf(Item("Banana", oct29, 0)))
    }
}
