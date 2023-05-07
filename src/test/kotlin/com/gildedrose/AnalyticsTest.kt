package com.gildedrose

import com.gildedrose.com.gildedrose.AnalyticsEvent
import com.gildedrose.com.gildedrose.LoggingAnalytics
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant

class AnalyticsTest {

    @Test
    fun `outputs json of events`() {
        val logged = mutableListOf<String>()
        val now =  Instant.parse("2023-05-07T12:41:57.474506Z")
        val analytics = LoggingAnalytics(
            logged::add,
            clock = { now }
        )


        assertThat(logged.size).isEqualTo(0)
        analytics(TestEvent("banana"))
        assertThat(logged).isEqualTo(listOf("""{"timestamp":"2023-05-07T12:41:57.474506Z","event":{"value":"banana","eventName":"TestEvent"}}"""))

    }

}

data class TestEvent(val value: String) : AnalyticsEvent
