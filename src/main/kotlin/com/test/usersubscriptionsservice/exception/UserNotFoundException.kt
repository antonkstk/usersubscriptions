package com.test.usersubscriptionsservice.exception

import java.util.UUID

class UserNotFoundException(userId: UUID): RuntimeException("User with id: <$userId> was not found")