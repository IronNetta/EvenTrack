package org.seba.eventrack.api.controllers;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.api.models.CustomPage;
import org.seba.eventrack.api.models.event.dtos.EventDto;
import org.seba.eventrack.bll.services.EventService;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.il.requests.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
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
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.findById(id));
    }


    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.save(event));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return ResponseEntity.ok(eventService.update(event));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
