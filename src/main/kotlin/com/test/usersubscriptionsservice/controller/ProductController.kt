package com.test.usersubscriptionsservice.controller

import com.test.usersubscriptionsservice.controller.message.ProductResponseListMessage
import com.test.usersubscriptionsservice.controller.message.ProductResponseMessage
import com.test.usersubscriptionsservice.entity.ProductEntity
import com.test.usersubscriptionsservice.service.ProductService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService
) {
    @GetMapping
    fun getAllProducts(): ProductResponseListMessage { // TODO: add pagination
        return productService.getAllProducts().toProductResponseListMessage()
    }

    @GetMapping("/{productId}")
    fun getProduct(@PathVariable productId: UUID): ProductResponseMessage {
        return productService.getProduct(productId).toProductResponseMessage()
    }

    private fun ProductEntity.toProductResponseMessage(): ProductResponseMessage {
        return ProductResponseMessage(
            id = this.id!!,
            name = this.name,
            description = this.description
        )
    }

    private fun List<ProductEntity>.toProductResponseListMessage(): ProductResponseListMessage {
        return ProductResponseListMessage(
            productList = this.map {
                ProductResponseMessage(
                    id = it.id!!,
                    name = it.name,
                    description = it.description
                )
            }
        )
    }
}