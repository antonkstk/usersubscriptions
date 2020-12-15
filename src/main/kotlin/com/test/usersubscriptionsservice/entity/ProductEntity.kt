package com.test.usersubscriptionsservice.entity

import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "products")
data class ProductEntity(
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    val id: UUID? = null,

    @Column(name = "name")
    val name: String,

    @Column(name = "description")
    val description: String,

    @Column(name = "duration")
    @Enumerated(EnumType.STRING)
    val duration: DurationPeriod,

    @Column(name = "price")
    val price: Double
) {
    enum class DurationPeriod {
        WEEK,
        MONTH,
        YEAR
    }
}