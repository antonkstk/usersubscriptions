package com.test.usersubscriptionsservice.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import org.hibernate.annotations.GenericGenerator
import java.time.ZonedDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "subscriptions")
data class SubscriptionEntity(
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    val id: UUID? = null,

    @Column(name = "start_date")
    val startDate: ZonedDateTime,

    @Column(name = "end_date")
    val endDate: ZonedDateTime,

    @Column(name = "price")
    val price: Double,

    @Column(name = "active")
    val active: Boolean = true,

    @Column(name = "deleted")
    val deleted: Boolean = false,

    @Column(name = "product_id")
    val productId: UUID? = null,

    @OneToOne(mappedBy = "subscription")
    @JsonManagedReference
    val user: UserEntity? = null
)
