package com.gildedrose.com.gildedrose

import com.gildedrose.Item
import java.time.LocalDate

fun update(items: List<Item>, days: Int, on: LocalDate) = items.map { item ->
    item.updatedBy(days, on)
}


