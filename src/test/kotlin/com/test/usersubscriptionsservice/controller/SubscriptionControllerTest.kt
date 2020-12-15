package com.test.usersubscriptionsservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.whenever
import com.test.usersubscriptionsservice.controller.message.SubscriptionDetailsResponse
import com.test.usersubscriptionsservice.controller.message.SubscriptionUpdateRequest
import com.test.usersubscriptionsservice.entity.SubscriptionEntity
import com.test.usersubscriptionsservice.exception.SubscriptionNotFoundException
import com.test.usersubscriptionsservice.service.SubscriptionService
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.ZonedDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
@WebMvcTest(SubscriptionController::class)
internal class SubscriptionControllerTest {

    @MockBean
    private lateinit var subscriptionService: SubscriptionService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun `Should return subscription of a user`() {
        // GIVEN
        val userId = UUID.randomUUID()
        val subscriptionId = UUID.randomUUID()
        val startDate = ZonedDateTime.now()
        val endDate = ZonedDateTime.now().plusWeeks(1)
        val subscriptionEntity = SubscriptionEntity(
            id = subscriptionId,
            startDate = startDate,
            endDate = endDate,
            price = 9.99
        )
        val response = SubscriptionDetailsResponse(
            id = subscriptionId,
            startDate = startDate,
            endDate = endDate,
            price = 9.99,
            active = true
        )

        whenever(subscriptionService.getSubscriptionDetails(userId)).thenReturn(subscriptionEntity)

        // WHEN
        mvc.perform(
            MockMvcRequestBuilders.get("/api/v1/subscriptions/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
        )
            // THEN
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.`is`(response.id.toString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", Matchers.`is`(response.startDate.withFixedOffsetZone().toString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.active", Matchers.`is`(response.active)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.`is`(response.price)))
    }

    @Test
    fun `Should return 404 if user's subscription was not found`() {
        // GIVEN
        val userId = UUID.randomUUID()

        whenever(subscriptionService.getSubscriptionDetails(userId)).thenThrow(SubscriptionNotFoundException())

        // WHEN
        mvc.perform(
            MockMvcRequestBuilders.get("/api/v1/subscriptions/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
        )
            // THEN
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `Should pause subscription`() {
        // GIVEN
        val subscriptionId = UUID.randomUUID()
        val subscriptionUpdateRequest = SubscriptionUpdateRequest(
            setActive = false
        )

        // WHEN
        mvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/subscriptions/{subscriptionId}", subscriptionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(subscriptionUpdateRequest))
        )
            // THEN
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `Should return 404 when subscription is not found during update`() {
        // GIVEN
        val subscriptionId = UUID.randomUUID()
        val subscriptionUpdateRequest = SubscriptionUpdateRequest(
            setActive = false
        )

        whenever(subscriptionService.updateSubscription(subscriptionId, subscriptionUpdateRequest))
            .thenThrow(SubscriptionNotFoundException())

        // WHEN
        mvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/subscriptions/{subscriptionId}", subscriptionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(subscriptionUpdateRequest))
        )
            // THEN
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `Should cancel subscription`() {
        // GIVEN
        val subscriptionId = UUID.randomUUID()

        // WHEN
        mvc.perform(
            MockMvcRequestBuilders.delete("/api/v1/subscriptions/{subscriptionId}", subscriptionId)
                .contentType(MediaType.APPLICATION_JSON)
        )
            // THEN
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}