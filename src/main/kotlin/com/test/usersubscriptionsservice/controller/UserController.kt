package com.test.usersubscriptionsservice.controller

import com.test.usersubscriptionsservice.controller.message.UserResponseListMessage
import com.test.usersubscriptionsservice.entity.UserEntity
import com.test.usersubscriptionsservice.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping
    fun getAllUsers(): UserResponseListMessage {
        return userService.getAllUsers().toUserResponseListMessage()
    }

    private fun List<UserEntity>.toUserResponseListMessage(): UserResponseListMessage {
        return UserResponseListMessage(
            users = this.map {
                UserResponseListMessage.UserDetails(
                    id = requireNotNull(it.id) { "User ID cannot be null" },
                    firstName = it.firstName,
                    lastName = it.lastName,
                    subscription = it.subscription
                )
            }
        )
    }
}