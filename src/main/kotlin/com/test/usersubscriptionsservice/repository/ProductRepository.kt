package com.test.usersubscriptionsservice.repository

import com.test.usersubscriptionsservice.entity.ProductEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.Repository
import java.util.UUID
import java.util.Optional

interface ProductRepository: Repository<ProductEntity, UUID>, JpaRepository<ProductEntity, UUID> {

    fun save(product: ProductEntity): ProductEntity

    override fun findById(id: UUID): Optional<ProductEntity>

    override fun findAll(pageable: Pageable): Page<ProductEntity>
}