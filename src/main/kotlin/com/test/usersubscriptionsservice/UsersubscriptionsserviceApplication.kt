package com.test.usersubscriptionsservice

import com.test.usersubscriptionsservice.entity.ProductEntity
import com.test.usersubscriptionsservice.entity.SubscriptionEntity
import com.test.usersubscriptionsservice.entity.UserEntity
import com.test.usersubscriptionsservice.repository.ProductRepository
import com.test.usersubscriptionsservice.repository.SubscriptionRepository
import com.test.usersubscriptionsservice.repository.UserRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@SpringBootApplication
class UsersubscriptionsserviceApplication

fun main(args: Array<String>) {
	val application = runApplication<UsersubscriptionsserviceApplication>(*args)

	/**
	 * Create test data
	 */
	val userRepository = application.getBean(UserRepository::class.java)
	val subscriptionRepository = application.getBean(SubscriptionRepository::class.java)
	val productRepository = application.getBean(ProductRepository::class.java)

	// Create User and associated subscription
	val userId1 = UUID.randomUUID()
	val subscriptionId1 = UUID.randomUUID()
	val subscription1 = SubscriptionEntity(
		id = subscriptionId1,
		startDate = ZonedDateTime.parse("2020-12-07T10:15:30", DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.systemDefault())),
		endDate = ZonedDateTime.parse("2020-12-14T10:15:30", DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.systemDefault())),
		price = 9.99
	)

	val user1 = UserEntity(
		id = userId1,
		firstName = "John",
		lastName = "Doe",
		subscription = subscriptionRepository.save(subscription1)
	)
	userRepository.save(user1)

	// Create User without subscription
	val user2 = UserEntity(
		firstName = "Max",
		lastName = "Mustermann"
	)
	userRepository.save(user2)

	// Create products
	val product1 = ProductEntity(
		name = "Full-Body Circuit",
		description = "Get toned",
		duration = ProductEntity.DurationPeriod.WEEK,
		price = 24.99
	)
	val product2 = ProductEntity(
		name = "Yoga",
		description = "Increase flexibility",
		duration = ProductEntity.DurationPeriod.MONTH,
		price = 69.99
	)
	val product3 = ProductEntity(
		name = "Fat burner",
		description = "Lose fat",
		duration = ProductEntity.DurationPeriod.YEAR,
		price = 134.99
	)
	productRepository.save(product1)
	productRepository.save(product2)
	productRepository.save(product3)

}
