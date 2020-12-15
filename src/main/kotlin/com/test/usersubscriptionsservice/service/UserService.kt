package com.test.usersubscriptionsservice.service

import com.test.usersubscriptionsservice.entity.UserEntity
import com.test.usersubscriptionsservice.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun getAllUsers(): List<UserEntity> = userRepository.findAll()

    fun getUser(id: UUID): UserEntity? = userRepository.findById(id)

    fun saveUser(user: UserEntity): UserEntity = userRepository.save(user)
}