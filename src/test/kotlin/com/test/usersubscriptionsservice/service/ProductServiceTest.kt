package com.test.usersubscriptionsservice.service

import com.nhaarman.mockitokotlin2.whenever
import com.test.usersubscriptionsservice.entity.ProductEntity
import com.test.usersubscriptionsservice.exception.ProductNotFoundException
import com.test.usersubscriptionsservice.repository.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

@ExtendWith(MockitoExtension::class)
internal class ProductServiceTest {

    @Mock
    private lateinit var productRepository: ProductRepository

    @InjectMocks
    private lateinit var productService: ProductService

    @Test
    fun `Should call repository to fetch single product`() {
        // GIVEN
        val productId = UUID.randomUUID()
        val productEntity = ProductEntity(
            id = productId,
            name = "productName",
            description = "productDescription"
        )
        val expectedResult = ProductEntity(
            id = productId,
            name = "productName",
            description = "productDescription"
        )

        whenever(productRepository.findById(productId)).thenReturn(productEntity)

        // WHEN
        val result = productService.getProduct(productId)

        // THEN
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `Should throw ProductNotFoundException`() {
        // GIVEN
        val productId = UUID.randomUUID()

        whenever(productRepository.findById(productId)).thenReturn(null)

        // WHEN THEN
        assertThatThrownBy { productService.getProduct(productId) }
            .isInstanceOf(ProductNotFoundException::class.java)
            .hasMessage("Product with id: <$productId> was not found")
    }

    @Test
    fun `Should call repository to fetch list of products`() {
        // GIVEN
        val productId1 = UUID.randomUUID()
        val productId2 = UUID.randomUUID()
        val productEntity1 = ProductEntity(
            id = productId1,
            name = "productName1",
            description = "productDescription1"
        )
        val productEntity2 = ProductEntity(
            id = productId2,
            name = "productName2",
            description = "productDescription2"
        )
        val expectedResult = listOf(productEntity1, productEntity2)

        whenever(productRepository.findAll()).thenReturn(listOf(productEntity1, productEntity2))

        // WHEN
        val result = productService.getAllProducts()

        // THEN
        assertThat(result).isEqualTo(expectedResult)
    }

 }