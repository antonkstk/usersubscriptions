package com.test.usersubscriptionsservice.repository

import com.test.usersubscriptionsservice.entity.UserEntity
import org.springframework.data.repository.Repository
import java.util.UUID

interface UserRepository: Repository<UserEntity, UUID> {

    fun findById(id: UUID): UserEntity?

    fun findAll(): List<UserEntity>

    fun save(user: UserEntity): UserEntity
}