package com.test.usersubscriptionsservice.controller

import com.test.usersubscriptionsservice.exception.ProductNotFoundException
import com.test.usersubscriptionsservice.exception.SubscriptionNotFoundException
import com.test.usersubscriptionsservice.exception.UserNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(ProductNotFoundException::class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Product not found")
    fun handleProductNotFoundException(ex: ProductNotFoundException) {
        logger.info(ex) { "Handle ProductNotFoundException: ${ex.message}" }
    }

    @ExceptionHandler(SubscriptionNotFoundException::class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Subscription not found")
    fun handleProductNotFoundException(ex: SubscriptionNotFoundException) {
        logger.info(ex) { "Handle SubscriptionNotFoundException: ${ex.message}" }
    }

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User not found")
    fun handleProductNotFoundException(ex: UserNotFoundException) {
        logger.info(ex) { "Handle UserNotFoundException: ${ex.message}" }
    }
}