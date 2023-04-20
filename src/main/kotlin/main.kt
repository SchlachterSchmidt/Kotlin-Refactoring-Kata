package com.gildedrose

import java.io.File

fun main() {
    val file = File("stock.tsv").also { it.createNewFile() }
    val stock = file.loadItems()
    stock.saveTo(file)

    val server = Server(routes(stock))
    server.start()
}
