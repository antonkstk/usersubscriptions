package com.test.usersubscriptionsservice.controller

import com.test.usersubscriptionsservice.controller.message.SubscriptionDetailsResponse
import com.test.usersubscriptionsservice.controller.message.SubscriptionUpdateRequest
import com.test.usersubscriptionsservice.entity.SubscriptionEntity
import com.test.usersubscriptionsservice.service.SubscriptionService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/subscriptions")
class SubscriptionController(
    private val subscriptionService: SubscriptionService
) {
    @GetMapping("/users/{userId}")
    fun getSubscriptionForUser(@PathVariable userId: UUID): SubscriptionDetailsResponse {
        return subscriptionService.getSubscriptionDetails(userId).toSubscriptionDetailsResponse()
    }

    @PatchMapping("/{subscriptionId}")
    fun updateSubscription(
        @PathVariable subscriptionId: UUID,
        @RequestBody subscriptionUpdateRequest: SubscriptionUpdateRequest
    ) {
        subscriptionService.updateSubscription(subscriptionId, subscriptionUpdateRequest)
    }

    @DeleteMapping("/{subscriptionId}")
    fun cancelSubscription(@PathVariable subscriptionId: UUID) {
        subscriptionService.cancelSubscription(subscriptionId)
    }

    private fun SubscriptionEntity.toSubscriptionDetailsResponse(): SubscriptionDetailsResponse {
        return SubscriptionDetailsResponse(
            id = requireNotNull(this.id) { "Subscription ID cannot be null" },
            startDate = this.startDate,
            endDate = this.endDate,
            price = this.price,
            active = this.active
        )
    }
}