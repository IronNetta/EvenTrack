package org.seba.eventrack.api.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.seba.eventrack.api.models.CustomPage;
import org.seba.eventrack.api.models.event.dtos.EventDto;
import org.seba.eventrack.api.models.event.forms.EventForm;
import org.seba.eventrack.bll.services.EventService;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.il.requests.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
@Tag(name = "Events", description = "Endpoints pour les événements")
public class EventController {

    private final EventService eventService;


    @GetMapping
    public ResponseEntity<CustomPage<EventDto>> getAllEvents(
            @RequestParam Map<String, String> params,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        List<SearchParam<Event>> searchParams = SearchParam.create(params);
        Page<Event> events = eventService.findAll(
                searchParams,
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort)));
        List<EventDto> dtos = events.getContent().stream()
                .map(EventDto::fromEvent)
                .toList();
        CustomPage<EventDto> result = new CustomPage<>(dtos,events.getTotalPages(),events.getNumber() + 1);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(EventDto.fromEvent(eventService.findById(id)));
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ORGANIZER')")
    @PostMapping
    public ResponseEntity<EventDto> createEvent(@RequestBody EventForm event) {
        return ResponseEntity.ok(EventDto.fromEvent(eventService.save(event.toEvent())));
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ORGANIZER')")
    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @RequestBody EventForm eventForm) {
        return ResponseEntity.ok(EventDto.fromEvent(eventService.update(eventForm.toEvent(), id)));
    }
    //TODO gérer le cas de la modification si c'est le user qui a créé l'event

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    //TODO gérer le cas de la suppression si c'est le user qui a créé l'event


    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/accept")
    public ResponseEntity<EventDto> acceptEvent(@PathVariable Long id) {
        return ResponseEntity.ok(EventDto.fromEvent(eventService.validateEvent(eventService.findById(id))));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<EventDto> rejectEvent(@PathVariable Long id) {
        return ResponseEntity.ok(EventDto.fromEvent(eventService.refuseEvent(eventService.findById(id))));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/planning")
    public ResponseEntity<CustomPage<EventDto>> getPlanningEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sort,
            @RequestParam int year,
            @RequestParam int month
    ) {
        Page<Event> events = eventService.findAllByDate(
                year,
                month,
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort))
        );
        List<EventDto> dtos = events.getContent().stream()
                .map(EventDto::fromEvent)
                .toList();
        CustomPage<EventDto> result = new CustomPage<>(dtos,events.getTotalPages(),events.getNumber() + 1);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/planify")
    public ResponseEntity<EventDto> planifyEvent(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestParam LocalDateTime date
            ) {
        return ResponseEntity.ok(EventDto.fromEvent(eventService.planifyEvent(eventService.findById(id), date)));
    }

    @PreAuthorize("hasAuthority('ORGANIZER')")
    @GetMapping("/popularity/{id}")
    public ResponseEntity<Double> getPopularity(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getPopularity(id));
    }
}
