package com.test.usersubscriptionsservice.exception

import java.util.UUID

class ProductNotFoundException(productId: UUID): RuntimeException("Product with id: <$productId> was not found")