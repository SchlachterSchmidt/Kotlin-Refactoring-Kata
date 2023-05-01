package com.gildedrose.com.gildedrose

import com.gildedrose.Item

fun update(items: List<Item>, days: Int) = items.map { item ->
    item.updatedBy(days)
}


