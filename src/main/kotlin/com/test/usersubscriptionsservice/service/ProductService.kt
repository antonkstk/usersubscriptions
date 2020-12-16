package com.test.usersubscriptionsservice.service

import com.test.usersubscriptionsservice.entity.ProductEntity
import com.test.usersubscriptionsservice.exception.ProductNotFoundException
import com.test.usersubscriptionsservice.repository.ProductRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val subscriptionService: SubscriptionService
) {

    fun getAllProducts(pageable: Pageable): List<ProductEntity> {
        return productRepository.findAll(pageable).content
    }

    fun getProduct(productId: UUID): ProductEntity {
        val productOptional = productRepository.findById(productId)
        return if (productOptional.isPresent) {
            productOptional.get()
        } else {
            throw ProductNotFoundException(productId)
        }
    }

    fun buyProduct(productId: UUID, userId: UUID) {
        subscriptionService.createSubscription(productId, userId)
    }
}