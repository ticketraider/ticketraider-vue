package com.codersgate.ticketraider.domain.review.model

import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.member.entity.Member
import com.codersgate.ticketraider.global.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table


@Entity
@Table(name = "reviews")
class Review (
    @Column(name = "title")
    var title : String,

    @Column(name = "content")
    var content : String,

    @Column(name = "rating")
    var rating : Int,

    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne
    @JoinColumn(name = "event_id")
    val event : Event,

    ) : BaseEntity()
    {
        @Id
        @GeneratedValue( strategy = GenerationType.IDENTITY)
        val id : Long? = null
}



