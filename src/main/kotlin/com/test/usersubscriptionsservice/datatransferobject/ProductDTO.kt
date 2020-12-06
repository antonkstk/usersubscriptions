package com.test.usersubscriptionsservice.datatransferobject

import java.util.UUID

data class ProductDTO(
    val id: UUID,
    val name: String,
    val description: String
)