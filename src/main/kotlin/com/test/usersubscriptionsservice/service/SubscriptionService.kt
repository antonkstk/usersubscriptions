package com.test.usersubscriptionsservice.service

import com.test.usersubscriptionsservice.controller.message.SubscriptionUpdateRequest
import com.test.usersubscriptionsservice.entity.ProductEntity
import com.test.usersubscriptionsservice.entity.ProductEntity.DurationPeriod
import com.test.usersubscriptionsservice.entity.SubscriptionEntity
import com.test.usersubscriptionsservice.exception.ProductNotFoundException
import com.test.usersubscriptionsservice.exception.SubscriptionNotFoundException
import com.test.usersubscriptionsservice.exception.UserNotFoundException
import com.test.usersubscriptionsservice.repository.ProductRepository
import com.test.usersubscriptionsservice.repository.SubscriptionRepository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.UUID
import javax.transaction.Transactional

@Service
class SubscriptionService(
    private val subscriptionRepository: SubscriptionRepository,
    private val productRepository: ProductRepository,
    private val userService: UserService
) {

    fun getSubscriptionDetails(userId: UUID): SubscriptionEntity {
        return subscriptionRepository.findByUserIdAndDeletedFalse(userId) ?: throw SubscriptionNotFoundException()
    }

    // TODO: consider updating endDate when resuming
    fun updateSubscription(id: UUID, subscriptionUpdateRequest: SubscriptionUpdateRequest) {
        val subscription = subscriptionRepository.findById(id) ?: throw SubscriptionNotFoundException()

        subscriptionRepository.save(subscription.copy(active = subscriptionUpdateRequest.setActive))
    }

    @Transactional
    fun createSubscription(productId: UUID, userId: UUID) {
        val product = getProduct(productId)
        val user = userService.getUser(userId) ?: throw UserNotFoundException(userId)

        val startDate = ZonedDateTime.now()
        val endDate = getEndDate(startDate, product.duration)
        val price = product.price
        val subscription = SubscriptionEntity(
            startDate = ZonedDateTime.now(),
            endDate = endDate,
            price = price,
            productId = productId,
            user = user
        )

        val savedSubscription = subscriptionRepository.save(subscription)
        userService.saveUser(user.copy(subscription = savedSubscription))
    }

    @Transactional
    fun cancelSubscription(id: UUID) {
        val subscription = subscriptionRepository.findById(id) ?: throw SubscriptionNotFoundException()

        subscriptionRepository.save(
            subscription.copy(active = false, deleted = true)
        )

        subscription.user?.let {
            userService.saveUser(
                it.copy(subscription = null)
            )
        }
    }

    private fun getEndDate(startDate: ZonedDateTime, subscriptionType: DurationPeriod): ZonedDateTime {
        return when(subscriptionType) {
            DurationPeriod.WEEK -> startDate.plusWeeks(1)
            DurationPeriod.MONTH -> startDate.plusMonths(1)
            DurationPeriod.YEAR -> startDate.plusYears(1)
        }
    }

    private fun getProduct(productId: UUID): ProductEntity {
        val productOptional = productRepository.findById(productId)
        return if (productOptional.isPresent) {
            productOptional.get()
        } else {
            throw ProductNotFoundException(productId)
        }
    }
}