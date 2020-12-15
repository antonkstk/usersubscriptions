package com.test.usersubscriptionsservice.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    val id: UUID? = null,

    @Column(name = "first_name")
    val firstName: String,

    @Column(name = "last_name")
    val lastName: String,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "user_subscription",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "subscription_id", referencedColumnName = "id")]
    )
    @JsonBackReference
    val subscription: SubscriptionEntity? = null
)
