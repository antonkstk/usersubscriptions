package com.test.usersubscriptionsservice.service

import com.test.usersubscriptionsservice.entity.ProductEntity
import com.test.usersubscriptionsservice.exception.ProductNotFoundException
import com.test.usersubscriptionsservice.repository.ProductRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val subscriptionService: SubscriptionService
) {

    fun getAllProducts(): List<ProductEntity> {
        return productRepository.findAll()
    }

    fun getProduct(productId: UUID): ProductEntity {
        return productRepository.findById(productId) ?: throw ProductNotFoundException(productId)
    }

    fun buyProduct(productId: UUID, userId: UUID) {
        subscriptionService.createSubscription(productId, userId)
    }
}