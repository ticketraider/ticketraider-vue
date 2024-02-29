package com.codersgate.ticketraider.domain.event.service

import com.codersgate.ticketraider.domain.category.repository.CategoryRepository
import com.codersgate.ticketraider.domain.event.dto.EventRequest
import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.event.repository.price.PriceRepository
import com.codersgate.ticketraider.domain.event.repository.seat.AvailableSeatRepository
import com.codersgate.ticketraider.domain.place.repository.PlaceRepository
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val eventRepository: EventRepository,
    private val priceRepository: PriceRepository,
    private val availableSeatRepository: AvailableSeatRepository,
    private val placeRepository: PlaceRepository
) : EventService {
    override fun createEvent(eventRequest: EventRequest) {
        val category = categoryRepository.findByIdOrNull(eventRequest.categoryId)
            ?: throw ModelNotFoundException("category", eventRequest.categoryId)
        val place = placeRepository.findPlaceByName(eventRequest.place)
            ?: throw ModelNotFoundException("place", 0)//예외 추가 필요함
        val (price, event) = eventRequest.toPriceAndEvent(category, place)
        event.price = price
        eventRepository.save(event)
        priceRepository.save(price)

        val date = eventRequest.startDate
        val duration = eventRequest.endDate.compareTo(eventRequest.startDate)
        for (i in 0..duration) {
            val seat = eventRequest.toAvailableSeat(event, place, date.plusDays(i.toLong()))
            event.availableSeats.add(seat)
            availableSeatRepository.save(seat)
        }
    }

    @Transactional
    override fun updateEvent(eventId: Long,eventRequest: EventRequest) {
        TODO()
    }

    @Transactional
    override fun deleteEvent(eventId: Long) {
        val event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        eventRepository.delete(event)
    }

    override fun getEventList(): List<EventResponse> {
//        val eventList = eventRepository.findAll()
//        return eventList.map { EventResponse.from( it ) }
        TODO()
    }

    override fun getEvent(eventId: Long): EventResponse {
        val event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        val price = priceRepository.findByIdOrNull(event.price!!.id)
            ?: throw ModelNotFoundException("Event", eventId)
        val seat = availableSeatRepository.findByIdOrNull(1)
            ?: throw ModelNotFoundException("Event", eventId)
        return EventResponse.from(event,price,seat)
    }

//    override fun getPaginatedEventList(pageable: Pageable, status: String?): Page<EventResponse>? {
//        return  eventRepository.findByPageable(pageable).map{EventResponse.from(it)}
//    }
}