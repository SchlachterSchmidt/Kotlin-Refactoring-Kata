package com.gildedrose.http4k

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.then
import org.http4k.filter.ServerFilters.CatchAll
import org.junit.jupiter.api.Test

class CatchAllFilterTests {

    private val filter = CatchAll { _ ->
        Response(INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `logs exception on error`() {
        val handler: HttpHandler = { error("deli berate") }

        val response = filter.then(handler).invoke(Request(GET, "/"))

        assertThat(response.status)
            .isEqualTo(INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `passes response on no error`() {
        val handler: HttpHandler = { Response(Status.OK) }

        val response = filter.then(handler).invoke(Request(GET, "/"))

        assertThat(response.status)
            .isEqualTo(Status.OK)
    }

}
