package com.test.usersubscriptionsservice.controller

import com.test.usersubscriptionsservice.exception.ProductNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(ProductNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleProductNotFoundException(ex: ProductNotFoundException) {
        logger.info(ex) { "Handle ProductNotFoundException: ${ex.message}" }
    }
}