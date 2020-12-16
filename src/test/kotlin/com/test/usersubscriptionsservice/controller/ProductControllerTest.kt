package com.test.usersubscriptionsservice.controller

import com.nhaarman.mockitokotlin2.whenever
import com.test.usersubscriptionsservice.controller.message.ProductResponseListMessage
import com.test.usersubscriptionsservice.controller.message.ProductResponseMessage
import com.test.usersubscriptionsservice.entity.ProductEntity
import com.test.usersubscriptionsservice.exception.ProductNotFoundException
import com.test.usersubscriptionsservice.exception.UserNotFoundException
import com.test.usersubscriptionsservice.service.ProductService
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@ExtendWith(SpringExtension::class)
@WebMvcTest(ProductController::class)
internal class ProductControllerTest {

    @MockBean
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun `Should return single product`() {
        // GIVEN
        val productId = UUID.randomUUID()
        val productEntity = ProductEntity(
            id = productId,
            name = "productName",
            description = "productDescription",
            duration = ProductEntity.DurationPeriod.WEEK,
            price = 24.99
        )
        val productResponseMessage = ProductResponseMessage(
            id = productId,
            name = "productName",
            description = "productDescription",
            duration = ProductEntity.DurationPeriod.WEEK,
            price = 24.99
        )

        whenever(productService.getProduct(productId)).thenReturn(productEntity)

        // WHEN
        mvc.perform(
            MockMvcRequestBuilders.get("/api/v1/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
        )
        // THEN
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.`is`(productResponseMessage.id.toString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.`is`(productResponseMessage.name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.`is`(productResponseMessage.description)))
    }

    @Test
    fun `Should return 404 when product with given ID was not found`() {
        // GIVEN
        val productId = UUID.randomUUID()

        whenever(productService.getProduct(productId)).thenThrow(ProductNotFoundException(productId))

        // WHEN
        mvc.perform(
            MockMvcRequestBuilders.get("/api/v1/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
        )
        // THEN
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Should return list of all products`() {
        // GIVEN
        val productId1 = UUID.randomUUID()
        val productId2 = UUID.randomUUID()
        val pageable = PageRequest.of(0, 2)
        val productEntity1 = ProductEntity(
            id = productId1,
            name = "productName1",
            description = "productDescription1",
            duration = ProductEntity.DurationPeriod.MONTH,
            price = 69.99
        )
        val productEntity2 = ProductEntity(
            id = productId2,
            name = "productName2",
            description = "productDescription2",
            duration = ProductEntity.DurationPeriod.YEAR,
            price = 134.99
        )
        val productResponseListMessage = ProductResponseListMessage(
            productList = listOf(
                ProductResponseMessage(
                    id = productId1,
                    name = "productName1",
                    description = "productDescription1",
                    duration = ProductEntity.DurationPeriod.MONTH,
                    price = 69.99
                ),
                ProductResponseMessage(
                    id = productId2,
                    name = "productName2",
                    description = "productDescription2",
                    duration = ProductEntity.DurationPeriod.YEAR,
                    price = 134.99
                )
            )
        )

        whenever(productService.getAllProducts(pageable)).thenReturn(listOf(productEntity1, productEntity2))

        // WHEN
        mvc.perform(
            MockMvcRequestBuilders.get("/api/v1/products?size=2&page=0")
                .contentType(MediaType.APPLICATION_JSON)
        )
        // THEN
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.productList.length()", Matchers.`is`(productResponseListMessage.productList.size)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.productList[0].id", Matchers.`is`(productResponseListMessage.productList[0].id.toString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.productList[0].name", Matchers.`is`(productResponseListMessage.productList[0].name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.productList[0].description", Matchers.`is`(productResponseListMessage.productList[0].description)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.productList[1].id", Matchers.`is`(productResponseListMessage.productList[1].id.toString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.productList[1].name", Matchers.`is`(productResponseListMessage.productList[1].name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.productList[1].description", Matchers.`is`(productResponseListMessage.productList[1].description)))
    }

    @Test
    fun `Should return 201 when user successfully buys a product`() {
        // GIVEN
        val productId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        // WHEN
        mvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products/{productId}/users/{userId}", productId, userId)
                .contentType(MediaType.APPLICATION_JSON)
        )
            // THEN
            .andExpect(status().isCreated)
    }

    @Test
    fun `Should return 404 when product is not found during the subscription creation`() {
        // GIVEN
        val productId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        whenever(productService.buyProduct(productId, userId)).thenThrow(ProductNotFoundException(productId))

        // WHEN
        mvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products/{productId}/users/{userId}", productId, userId)
                .contentType(MediaType.APPLICATION_JSON)
        )
            // THEN
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Should return 404 when user is not found during the subscription creation`() {
        // GIVEN
        val productId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        whenever(productService.buyProduct(productId, userId)).thenThrow(UserNotFoundException(userId))

        // WHEN
        mvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products/{productId}/users/{userId}", productId, userId)
                .contentType(MediaType.APPLICATION_JSON)
        )
            // THEN
            .andExpect(status().isNotFound)
    }
}