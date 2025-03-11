package org.seba.eventrack.bll.services.impls;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seba.eventrack.bll.exceptions.user.UserAlreadyExistExeption;
import org.seba.eventrack.bll.exceptions.user.UserNotFoundException;
import org.seba.eventrack.dal.repositories.TicketRepository;
import org.seba.eventrack.dal.repositories.UserRepository;
import org.seba.eventrack.dl.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    // Initialisation des données avant chaque test
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("password");
    }

    // Vérifie que la récupération des utilisateurs fonctionne correctement
    @Test
    void getUsers_ShouldReturnPageOfUsers() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(pageRequest)).thenReturn(userPage);

        Page<User> result = userService.getUsers(List.of(), pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(user.getEmail(), result.getContent().get(0).getEmail());
    }

    // Vérifie la récupération d'un utilisateur par email lorsqu'il existe
    @Test
    void getUserByEmail_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        User foundUser = userService.getUserByEmail("test@example.com");
        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    // Vérifie qu'une exception est levée si l'utilisateur n'existe pas
    @Test
    void getUserByEmail_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("notfound@example.com"));
    }

    // Vérifie que l'enregistrement d'un utilisateur fonctionne
    @Test
    void saveUser_ShouldSaveUser_WhenNewUser() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    // Vérifie qu'une exception est levée si l'utilisateur existe déjà
    @Test
    void saveUser_ShouldThrowException_WhenUserAlreadyExists() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        assertThrows(UserAlreadyExistExeption.class, () -> userService.saveUser(user));
    }

    // Vérifie que la mise à jour d'un utilisateur fonctionne
    @Test
    void updateUser_ShouldUpdateUser_WhenUserExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(user, user.getEmail());
        assertNotNull(updatedUser);
        assertEquals(user.getEmail(), updatedUser.getEmail());
    }

    // Vérifie que la suppression d'un utilisateur fonctionne
    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(ticketRepository).deleteByParticipant(user);
        doNothing().when(userRepository).delete(user);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).delete(user);
    }

    // Vérifie qu'une exception est levée si l'utilisateur à supprimer n'existe pas
    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }

    // Vérifie que la récupération d'un utilisateur par ID fonctionne
    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User foundUser = userService.getUserById(1L);
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
    }

    // Vérifie qu'une exception est levée si l'utilisateur n'est pas trouvé par ID
    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }
}