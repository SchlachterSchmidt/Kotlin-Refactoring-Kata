package com.gildedrose.com.gildedrose

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_WITH_ZONE_ID
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import java.time.Instant

typealias Analytics = (AnalyticsEvent) -> Unit

interface AnalyticsEvent {
    val eventName: String get() = this::class.simpleName ?: "Event Name Unknown"
}

class LoggingAnalytics(
    private val logger: (String) -> Unit,
    private val mapper: ObjectMapper = loggingObjectMapper(),
    private val clock: () -> Instant = Instant::now
) : Analytics {
    override fun invoke(event: AnalyticsEvent) {
        logger(mapper.writeValueAsString(Envelope(clock(), event)))
    }

    class Envelope(
        val timestamp: Instant,
        val event: AnalyticsEvent
    )
}

fun loggingObjectMapper(): ObjectMapper = ObjectMapper().apply {
    registerModule(ParameterNamesModule())
    registerModule(Jdk8Module())
    registerModule(JavaTimeModule())
    disable(WRITE_DATES_AS_TIMESTAMPS)
    disable(WRITE_DATES_WITH_ZONE_ID)
}
