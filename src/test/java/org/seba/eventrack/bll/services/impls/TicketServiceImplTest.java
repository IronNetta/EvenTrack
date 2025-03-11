package org.seba.eventrack.bll.services.impls;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seba.eventrack.api.models.ticket.dtos.TicketDto;
import org.seba.eventrack.dal.repositories.EventRepository;
import org.seba.eventrack.dal.repositories.TicketRepository;
import org.seba.eventrack.dal.repositories.UserRepository;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.Ticket;
import org.seba.eventrack.dl.entities.User;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class) // Utilise Mockito pour les tests unitaires
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository; // Mock du repository des tickets

    @Mock
    private EventRepository eventRepository; // Mock du repository des événements

    @Mock
    private UserRepository userRepository; // Mock du repository des utilisateurs

    @InjectMocks
    private TicketServiceImpl ticketService; // Injection des mocks dans le service testé

    private Ticket ticket;
    private Event event;
    private User user;

    @BeforeEach
    void setUp() {
        // Initialisation des objets test
        event = new Event();
        event.setId(1L);
        event.setCapacity(100);
        event.setReservedSeats(50);

        user = new User();
        user.setId(1L);

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setEvent(event);
        ticket.setParticipant(user);
    }

    @Test
    void findById_ShouldReturnTicket_WhenTicketExists() {
        // Simule la récupération d'un ticket existant par son ID
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        // Appelle le service pour rechercher le ticket
        TicketDto foundTicket = ticketService.findById(1L);

        // Vérifie que le ticket est bien trouvé et que l'ID est correct
        assertNotNull(foundTicket);
        assertEquals(1L, foundTicket.getId());
    }

    @Test
    void findById_ShouldThrowException_WhenTicketNotFound() {
        // Simule l'absence d'un ticket avec l'ID donné
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        // Vérifie que l'appel du service lève une exception
        assertThrows(ResponseStatusException.class, () -> ticketService.findById(1L));
    }

    @Test
    void findByQrCodeUrl_ShouldReturnTicket_WhenQrCodeExists() {
        // Simule la récupération d'un ticket existant par son URL de QR Code
        when(ticketRepository.findByQrCodeUrl("qr123"))
                .thenReturn(Optional.of(ticket));

        // Appelle le service pour rechercher le ticket
        Ticket foundTicket = ticketService.findByQrCodeUrl("qr123");

        // Vérifie que le ticket est bien trouvé et que l'ID est correct
        assertNotNull(foundTicket);
        assertEquals(1L, foundTicket.getId());
    }

    @Test
    void findByQrCodeUrl_ShouldThrowException_WhenQrCodeNotFound() {
        // Simule l'absence d'un ticket avec l'URL de QR Code donné
        when(ticketRepository.findByQrCodeUrl("qr123"))
                .thenReturn(Optional.empty());

        // Vérifie que l'appel du service lève une exception
        assertThrows(ResponseStatusException.class, () -> ticketService.findByQrCodeUrl("qr123"));
    }

    @Test
    void cancelTicket_ShouldDeleteTicket_WhenExists() {
        // Simule la récupération d'un ticket existant
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        // Simule la suppression sans erreur du ticket
        doNothing().when(ticketRepository).delete(ticket);

        // Appelle le service pour annuler le ticket
        ticketService.cancelTicket(1L);

        // Vérifie que la méthode delete a bien été appelée une seule fois
        verify(ticketRepository, times(1)).delete(ticket);
    }

    @Test
    void cancelTicket_ShouldThrowException_WhenTicketNotFound() {
        // Simule l'absence d'un ticket avec l'ID donné
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        // Vérifie que l'appel du service lève une exception
        assertThrows(ResponseStatusException.class, () -> ticketService.cancelTicket(1L));
    }
}