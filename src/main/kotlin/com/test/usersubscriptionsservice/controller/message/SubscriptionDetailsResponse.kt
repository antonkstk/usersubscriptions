package com.test.usersubscriptionsservice.controller.message

import java.time.ZonedDateTime
import java.util.UUID

data class SubscriptionDetailsResponse(
    val id: UUID,
    val startDate: ZonedDateTime,
    val endDate: ZonedDateTime,
    val price: Double,
    val active: Boolean
)