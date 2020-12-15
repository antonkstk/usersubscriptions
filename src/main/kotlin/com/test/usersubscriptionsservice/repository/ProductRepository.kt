package com.test.usersubscriptionsservice.repository

import com.test.usersubscriptionsservice.entity.ProductEntity
import org.springframework.data.repository.Repository
import java.util.UUID

interface ProductRepository: Repository<ProductEntity, UUID> {

    fun save(product: ProductEntity): ProductEntity

    fun findById(id: UUID): ProductEntity?

    fun findAll(): List<ProductEntity>
}