package com.gildedrose

import java.io.File
import java.time.LocalDate

fun main() {
    val file = File("stock.tsv").also { it.createNewFile() }
    val stock = file.loadItems()

    stock.printout(LocalDate.now()).forEach(::println)
}
