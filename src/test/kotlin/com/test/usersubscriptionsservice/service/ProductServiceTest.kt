package com.test.usersubscriptionsservice.service

import com.nhaarman.mockitokotlin2.verify
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
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.UUID
import java.util.Optional

@ExtendWith(MockitoExtension::class)
internal class ProductServiceTest {

    @Mock
    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var subscriptionService: SubscriptionService

    @InjectMocks
    private lateinit var productService: ProductService

    @Test
    fun `Should call repository to fetch single product`() {
        // GIVEN
        val productId = UUID.randomUUID()
        val productEntity = ProductEntity(
            id = productId,
            name = "productName",
            description = "productDescription",
            duration = ProductEntity.DurationPeriod.MONTH,
            price = 69.99
        )
        val expectedResult = ProductEntity(
            id = productId,
            name = "productName",
            description = "productDescription",
            duration = ProductEntity.DurationPeriod.MONTH,
            price = 69.99
        )

        whenever(productRepository.findById(productId)).thenReturn(Optional.ofNullable(productEntity))

        // WHEN
        val result = productService.getProduct(productId)

        // THEN
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `Should throw ProductNotFoundException`() {
        // GIVEN
        val productId = UUID.randomUUID()

        whenever(productRepository.findById(productId)).thenReturn(Optional.empty())

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
            description = "productDescription1",
            duration = ProductEntity.DurationPeriod.YEAR,
            price = 134.99
        )
        val productEntity2 = ProductEntity(
            id = productId2,
            name = "productName2",
            description = "productDescription2",
            duration = ProductEntity.DurationPeriod.YEAR,
            price = 134.99
        )
        val pageable = PageRequest.of(0, 2)
        val expectedResult = listOf(productEntity1, productEntity2)
        val products: Page<ProductEntity> = PageImpl(expectedResult)

        whenever(productRepository.findAll(pageable)).thenReturn(products)

        // WHEN
        val result = productService.getAllProducts(pageable)

        // THEN
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `Should call subscriptionService to create new subscription`() {
        // GIVEN
        val productId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        // WHEN
        productService.buyProduct(productId, userId)

        // THEN
        verify(subscriptionService).createSubscription(productId, userId)
    }
 }