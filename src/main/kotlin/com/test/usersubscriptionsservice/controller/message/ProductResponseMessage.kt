package com.test.usersubscriptionsservice.controller.message

import java.util.UUID

data class ProductResponseMessage(
    val id: UUID,
    val name: String,
    val description: String
)