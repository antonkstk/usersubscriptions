package com.test.usersubscriptionsservice.repository

import com.test.usersubscriptionsservice.entity.SubscriptionEntity
import org.springframework.data.repository.Repository
import java.util.UUID

interface SubscriptionRepository: Repository<SubscriptionEntity, UUID> {

    fun save(subscriptionEntity: SubscriptionEntity): SubscriptionEntity

    fun findById(id: UUID): SubscriptionEntity?

    fun findByUserIdAndDeletedFalse(userId: UUID): SubscriptionEntity?
}