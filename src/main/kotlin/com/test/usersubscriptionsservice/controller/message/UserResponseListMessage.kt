package com.test.usersubscriptionsservice.controller.message

import com.test.usersubscriptionsservice.entity.SubscriptionEntity
import java.util.UUID

data class UserResponseListMessage(
    val users: List<UserDetails>
) {
    data class UserDetails(
        val id: UUID,
        val firstName: String,
        val lastName: String,
        val subscription: SubscriptionEntity?
    )
}