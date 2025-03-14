package org.seba.eventrack.bll.services.impls;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seba.eventrack.dal.repositories.EventRepository;
import org.seba.eventrack.dal.repositories.TicketRepository;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.enums.EventStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class) // Utilise Mockito pour l'injection automatique des mocks
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository; // Mock du repository des événements

    @Mock
    private TicketRepository ticketRepository; // Mock du repository des tickets

    @InjectMocks
    private EventServiceImpl eventService; // Injection des mocks dans le service testé

    private Event event;

    @BeforeEach
    void setUp() {
        // Création d'un événement test avec des valeurs par défaut
        event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setDate(LocalDateTime.now());
        event.setEventStatus(EventStatus.PENDING);
        event.setCapacity(100);
        event.setReservedSeats(50);
    }

    @Test
    void findById_ShouldReturnEvent_WhenEventExists() {
        // Simule la récupération d'un événement existant
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event foundEvent = eventService.findById(1L);

        assertNotNull(foundEvent);
        assertEquals(1L, foundEvent.getId());
    }

    @Test
    void findById_ShouldThrowException_WhenEventNotFound() {
        // Simule un événement non trouvé
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> eventService.findById(1L));
    }

    @Test
    void save_ShouldReturnSavedEvent() {
        // Simule la sauvegarde d'un événement
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event savedEvent = eventService.save(event);

        assertNotNull(savedEvent);
        assertEquals("Test Event", savedEvent.getTitle());
    }

    @Test
    void deleteById_ShouldThrowException_WhenEventDoesNotExist() {
        // Vérifie que la suppression lève une exception si l'événement n'existe pas
        when(eventRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> eventService.deleteById(1L));
    }

    @Test
    void getRatioOfReservedSeats_ShouldReturnCorrectValue_WhenEventExists() {
        // Simule un ratio de places réservées
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.getRatioOfReservedSeats(1L)).thenReturn(0.5);

        double ratio = eventService.getRatioOfReservedSeats(1L);

        assertEquals(0.5, ratio);
    }

    @Test
    void getRatioOfReservedSeats_ShouldThrowException_WhenEventDoesNotExist() {
        // Vérifie que l'exception est levée si l'événement n'existe pas
        when(eventRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> eventService.getRatioOfReservedSeats(1L));
    }

    @Test
    void getPopularity_ShouldReturnZero_WhenEventNotFull() {
        // Simule un événement non complet
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        double popularity = eventService.getPopularity(1L);

        assertEquals(0.0, popularity);
    }

    @Test
    void validateEvent_ShouldSetStatusAccepted_WhenEventExists() {
        // Vérifie que l'événement est bien validé
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event validatedEvent = eventService.validateEvent(event);

        assertEquals(EventStatus.ACCEPTED, validatedEvent.getEventStatus());
    }

    @Test
    void refuseEvent_ShouldSetStatusRejected_WhenEventExists() {
        // Vérifie que l'événement est bien refusé
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event refusedEvent = eventService.refuseEvent(event);

        assertEquals(EventStatus.REJECTED, refusedEvent.getEventStatus());
    }
}
