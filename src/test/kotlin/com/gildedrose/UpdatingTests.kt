package com.gildedrose

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test

class UpdatingTests {

    @Test
    fun `items decrease in quality one per day`() {
        assertThat(Item("Banana", oct29, 42).updatedBy(days = 1, on = oct29))
            .isEqualTo(Item("Banana", oct29, 41))
    }

    @Test
    fun `items decrease in quality by two per two days`() {
        assertThat(Item("Banana", oct29, 42).updatedBy(days = 2, on = oct29))
            .isEqualTo(Item("Banana", oct29, 40))
    }
    @Test
    fun `items decrease in quality by 0 for same day`() {
        assertThat(Item("Banana", oct29, 42).updatedBy(days = 0, on = oct29))
            .isEqualTo(Item("Banana", oct29, 42))
    }

    @Test
    fun `quality doesnt decrease past zero `() {
        assertThat(Item("Banana", oct29, 0).updatedBy(days = 1, on = oct29))
            .isEqualTo(Item("Banana", oct29, 0))

        assertThat(Item("Banana", oct29, 1).updatedBy(days = 2, on = oct29))
            .isEqualTo(Item("Banana", oct29, 0))
    }

    @Test
    fun `items decrease in quality by two per day after sell by date`() {
        assertThat(Item("Banana", oct29, 42).updatedBy(days = 1, on = oct29.plusDays(1)))
            .isEqualTo(Item("Banana", oct29, 40))

        assertThat(Item("Banana", oct29, 42).updatedBy(days = 0, on = oct29.plusDays(1)))
            .isEqualTo(Item("Banana", oct29, 42))

        assertThat(Item("Banana", oct29, 42).updatedBy(days = 2, on = oct29.plusDays(2)))
            .isEqualTo(Item("Banana", oct29, 38))

        assertThat(Item("Banana", oct29, 42).updatedBy(days = 2, on = oct29.plusDays(1)))
            .isEqualTo(Item("Banana", oct29, 39 ))
    }
}
