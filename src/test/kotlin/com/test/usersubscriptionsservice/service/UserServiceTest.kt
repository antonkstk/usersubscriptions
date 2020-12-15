package com.test.usersubscriptionsservice.service

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.test.usersubscriptionsservice.entity.UserEntity
import com.test.usersubscriptionsservice.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

@ExtendWith(MockitoExtension::class)
internal class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    @Test
    fun `Should return list of users`() {
        // GIVEN
        val users = listOf(
            UserEntity(
                id = UUID.randomUUID(),
                firstName = "firstName",
                lastName = "lastName"
            )
        )

        whenever(userRepository.findAll()).thenReturn(users)

        // WHEN
        val result = userService.getAllUsers()

        // THEN
        assertThat(result).isEqualTo(users)
    }

    @Test
    fun `Should return single user`() {
        // GIVEN
        val userId = UUID.randomUUID()
        val user = UserEntity(
            id = userId,
            firstName = "firstName",
            lastName = "lastName"
        )

        whenever(userRepository.findById(userId)).thenReturn(user)

        // WHEN
        val result = userService.getUser(userId)

        // THEN
        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `Should save user`() {
        // GIVEN
        val userId = UUID.randomUUID()
        val user = UserEntity(
            id = userId,
            firstName = "firstName",
            lastName = "lastName"
        )

        // WHEN
        userService.saveUser(user)

        // THEN
        verify(userRepository).save(user)
    }
}