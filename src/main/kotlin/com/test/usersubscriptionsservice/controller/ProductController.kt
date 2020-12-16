package com.test.usersubscriptionsservice.controller

import com.test.usersubscriptionsservice.controller.message.ProductResponseListMessage
import com.test.usersubscriptionsservice.controller.message.ProductResponseMessage
import com.test.usersubscriptionsservice.entity.ProductEntity
import com.test.usersubscriptionsservice.service.ProductService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService
) {
    @GetMapping
    fun getAllProducts(pageable: Pageable): ProductResponseListMessage {
        return productService.getAllProducts(pageable).toProductResponseListMessage()
    }

    @GetMapping("/{productId}")
    fun getProduct(@PathVariable productId: UUID): ProductResponseMessage {
        return productService.getProduct(productId).toProductResponseMessage()
    }

    @PostMapping("/{productId}/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun buyProduct(@PathVariable productId: UUID, @PathVariable userId: UUID) {
        productService.buyProduct(productId, userId)
    }

    private fun ProductEntity.toProductResponseMessage(): ProductResponseMessage {
        return ProductResponseMessage(
            id = requireNotNull(this.id) { "Product ID cannot be null" },
            name = this.name,
            description = this.description,
            duration = this.duration,
            price = this.price
        )
    }

    private fun List<ProductEntity>.toProductResponseListMessage(): ProductResponseListMessage {
        return ProductResponseListMessage(
            productList = this.map {
                ProductResponseMessage(
                    id = it.id!!,
                    name = it.name,
                    description = it.description,
                    duration = it.duration,
                    price = it.price
                )
            }
        )
    }
}