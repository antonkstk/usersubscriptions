package com.test.usersubscriptionsservice.service

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.test.usersubscriptionsservice.controller.message.SubscriptionUpdateRequest
import com.test.usersubscriptionsservice.entity.ProductEntity
import com.test.usersubscriptionsservice.entity.SubscriptionEntity
import com.test.usersubscriptionsservice.entity.UserEntity
import com.test.usersubscriptionsservice.exception.SubscriptionNotFoundException
import com.test.usersubscriptionsservice.repository.ProductRepository
import com.test.usersubscriptionsservice.repository.SubscriptionRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.ZonedDateTime
import java.util.UUID

@ExtendWith(MockitoExtension::class)
internal class SubscriptionServiceTest {

    @Mock
    private lateinit var subscriptionRepository: SubscriptionRepository

    @Mock
    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var subscriptionService: SubscriptionService

    @Test
    fun `Should call fetch subscription from db`() {
        // GIVEN
        val userId = UUID.randomUUID()
        val startDate = ZonedDateTime.now()
        val subscription = SubscriptionEntity(
            id = UUID.randomUUID(),
            startDate = startDate,
            endDate = startDate.plusWeeks(1),
            price = 9.99
        )

        whenever(subscriptionRepository.findByUserIdAndDeletedFalse(userId)).thenReturn(subscription)

        // WHEN
        val result = subscriptionService.getSubscriptionDetails(userId)

        // THEN
        assertThat(result).isEqualTo(subscription)
    }

    @Test
    fun `Should throw SubscriptionNotFoundException`() {
        // GIVEN
        val userId = UUID.randomUUID()

        whenever(subscriptionRepository.findByUserIdAndDeletedFalse(userId)).thenReturn(null)

        // WHEN THEN
        assertThatThrownBy {
            subscriptionService.getSubscriptionDetails(userId)
        }.isInstanceOf(SubscriptionNotFoundException::class.java)
    }

    @Test
    fun `Should call subscriptionRepository to pause subscription`() {
        // GIVEN
        val subscriptionId = UUID.randomUUID()
        val startDate = ZonedDateTime.now()
        val subscription = SubscriptionEntity(
            id = subscriptionId,
            startDate = startDate,
            endDate = startDate.plusWeeks(1),
            price = 9.99
        )
        val subscriptionUpdateRequest = SubscriptionUpdateRequest(
            setActive = false
        )

        whenever(subscriptionRepository.findById(subscriptionId)).thenReturn(subscription)

        // WHEN
        subscriptionService.updateSubscription(subscriptionId, subscriptionUpdateRequest)

        // THEN
        verify(subscriptionRepository).save(subscription.copy(active = false))
    }

    @Test
    fun `Should create subscription`() {
        // GIVEN
        val productId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val subscriptionId = UUID.randomUUID()
        val startDate = ZonedDateTime.now()
        val product = ProductEntity(
            id = productId,
            name = "name",
            description = "description",
            duration = ProductEntity.DurationPeriod.WEEK,
            price = 9.99
        )
        val user = UserEntity(
            id = userId,
            firstName = "John",
            lastName = "Doe"
        )
        val subscription = SubscriptionEntity(
            id = subscriptionId,
            startDate = startDate,
            endDate = startDate.plusWeeks(1),
            price = 9.99,
            productId = productId
        )

        whenever(productRepository.findById(productId)).thenReturn(product)
        whenever(userService.getUser(userId)).thenReturn(user)
        whenever(subscriptionRepository.save(any())).thenReturn(subscription)

        // WHEN
        subscriptionService.createSubscription(productId, userId)

        // THEN
        verify(userService).saveUser(user.copy(subscription = subscription))
    }

    @Test
    fun `Should cancel subscription`() {
        // GIVEN
        val userId = UUID.randomUUID()
        val subscriptionId = UUID.randomUUID()
        val startDate = ZonedDateTime.now()
        val user = UserEntity(
            id = userId,
            firstName = "John",
            lastName = "Doe"
        )
        val subscription = SubscriptionEntity(
            id = subscriptionId,
            startDate = startDate,
            endDate = startDate.plusWeeks(1),
            price = 9.99,
            productId = UUID.randomUUID(),
            user = user
        )

        whenever(subscriptionRepository.findById(subscriptionId)).thenReturn(subscription)

        // WHEN
        subscriptionService.cancelSubscription(subscriptionId)

        // THEN
        verify(subscriptionRepository).save(subscription.copy(active = false, deleted = true))
        verify(userService).saveUser(user.copy(subscription = null))
    }
}