package com.test.usersubscriptionsservice.controller.message

import com.test.usersubscriptionsservice.entity.ProductEntity
import java.util.UUID

data class ProductResponseMessage(
    val id: UUID,
    val name: String,
    val description: String,
    val duration: ProductEntity.DurationPeriod,
    val price: Double
)