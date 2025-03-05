package org.seba.eventrack.il.initializer;


import org.seba.eventrack.dal.repositories.EventRepository;
import org.seba.eventrack.dal.repositories.TicketRepository;
import org.seba.eventrack.dal.repositories.UserRepository;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.Ticket;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.dl.enums.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;


    public DataInitializer(UserRepository userRepository, EventRepository eventRepository, TicketRepository ticketRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        loadUsers();
        loadEvents();
        loadTickets();
    }


    private void loadUsers() {
        if (userRepository.count() == 0) {
            String password = passwordEncoder.encode("password");
            User admin = new User("admin", "admin@email.com", password, UserRole.ADMIN);
            User organizer = new User("organizer", "organizer@email.com", password, UserRole.ORGANIZER);
            User participant = new User("participant", "participant@email.com", password, UserRole.PARTICIPANT);

            userRepository.saveAll(List.of(admin, organizer, participant));
        }
    }

    private void loadEvents() {
        if (eventRepository.count() == 0) {
            Event concert = new Event("Rock Festival", "Un super concert de rock", LocalDateTime.now().plusDays(10), "Bruxelles", 500);
            Event conference = new Event("Tech Conference", "Conférence sur les nouvelles technologies", LocalDateTime.now().plusDays(20), "Paris", 300);
            Event sport = new Event("FC Barcelone vs Real Madrid CF", "Le match qui va départager le championnat espagnol", LocalDateTime.now().plusDays(30), "Barcelone", 100000);

            eventRepository.saveAll(List.of(concert, conference, sport));
        }
    }

    private void loadTickets() {
        if (ticketRepository.count() == 0) {
            User user = userRepository.findByEmail("participant@email.com").orElse(null);
            Event event = eventRepository.findByTitle("Rock Festival");

            if (user != null && event != null) {
                Ticket ticket = new Ticket(user, event);
                ticketRepository.save(ticket);
            }
        }
    }
}
