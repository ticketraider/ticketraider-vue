package com.codersgate.ticketraider.domain.ticket.entity

import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.member.entity.Member
import com.codersgate.ticketraider.global.common.BaseEntity
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime

@Table(name = "tickets")
@SQLDelete(sql = "UPDATE tickets SET is_deleted = true WHERE id = ?") // DELETE 쿼리 날아올 시 대신 실행
@SQLRestriction("is_deleted = false")
@Entity
class Ticket(

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer::class)
    @JsonDeserialize(using = LocalDateDeserializer::class)
    @Column(name = "date")
    val date: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(name = "grade", nullable = false)
    var grade: TicketGrade,

    @Column(name = "seatNo", nullable = false)
    val seatNo: Int,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    val event: Event,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_status", nullable = false)
    var ticketStatus: TicketStatus,

    @Column(name = "price", nullable = false)
    val price: Int,

    @Column(name = "place", nullable = false)
    val place: String

): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun toggleTicketStatus() {
        ticketStatus = if (ticketStatus == TicketStatus.UNPAID) {
            TicketStatus.PAID
        } else TicketStatus.UNPAID
    }
}