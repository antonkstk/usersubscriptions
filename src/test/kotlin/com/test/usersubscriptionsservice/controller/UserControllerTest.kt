package com.test.usersubscriptionsservice.controller

import com.nhaarman.mockitokotlin2.whenever
import com.test.usersubscriptionsservice.entity.UserEntity
import com.test.usersubscriptionsservice.service.UserService
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
import java.util.UUID

@ExtendWith(SpringExtension::class)
@WebMvcTest(UserController::class)
internal class UserControllerTest {

    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun `Should return list of users`() {
        val user = UserEntity(
            id = UUID.randomUUID(),
            firstName = "firstName",
            lastName = "lastName"
        )

        whenever(userService.getAllUsers()).thenReturn(listOf(user))

        // WHEN
        mvc.perform(
            MockMvcRequestBuilders.get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
        )
            // THEN
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.users[0].id", Matchers.`is`(user.id.toString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.users[0].firstName", Matchers.`is`(user.firstName)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.users[0].lastName", Matchers.`is`(user.lastName)))

    }
}