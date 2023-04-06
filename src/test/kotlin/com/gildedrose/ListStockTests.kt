package com.gildedrose

import org.assertj.core.api.Assertions
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ListStockTests {

    private val now = LocalDate.parse("2023-04-05")

    @Test
    fun `print non-empty stock list`() {
        val stock = listOf(
            Item("banana", now.minusDays(1), 42u),
            Item("kumquat", now.plusDays(1), 101u)
        )

        val server = Server(stock) { now }
        val client = server.routes

        val response = client(Request(GET, "/"))
        Assertions.assertThat(response.bodyString()).isEqualTo(expected)
    }
}

private val expected = """
    <html lang="en">
    <body>
    <h1>April 5, 2023</h1>
    <table>
    <tr>
        <th>Name</th>
        <th>Sell By Date</th>
        <th>Sell By Days</th>
        <th>Quality</th>
    </tr>
    <tr>
        <td>banana</td>
        <td>April 4, 2023</td>
        <td>-1</td>
        <td>42</td>
    </tr>
    <tr>
        <td>kumquat</td>
        <td>April 6, 2023</td>
        <td>1</td>
        <td>101</td>
    </tr>

    </table>
    </body>
    </html>
""".trimIndent()
