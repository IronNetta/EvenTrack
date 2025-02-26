package org.seba.eventrack.api.controllers;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.api.models.security.dtos.UserSessionDTO;
import org.seba.eventrack.bll.services.UserService;
import org.seba.eventrack.dl.entities.User;
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
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    @GetMapping
    public ResponseEntity<Page<UserSessionDTO>> getAllUsers(
            @RequestParam Map<String, String> params,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "id") String sort
    ) {
        List<SearchParam<User>> searchParams = SearchParam.create(params);
        Page<User> users = userService.getUsers(
                searchParams,
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort))
        );
        Page<UserSessionDTO> dtos = users.map(UserSessionDTO::fromUser);
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('ADMIN') or #email == authentication.principal.username")
    @GetMapping("/{email}")
    public ResponseEntity<UserSessionDTO> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(UserSessionDTO.fromUser(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserSessionDTO> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(UserSessionDTO.fromUser(savedUser));
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<UserSessionDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(UserSessionDTO.fromUser(updatedUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
