package org.seba.eventrack.api.controllers;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.api.models.CustomPage;
import org.seba.eventrack.api.models.security.dtos.UserSessionDTO;
import org.seba.eventrack.api.models.user.dtos.UserDTO;
import org.seba.eventrack.api.models.user.forms.UserForm;
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<CustomPage<UserSessionDTO>> getAllUsers(
            @RequestParam Map<String, String> params,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "lastName") String sort

    ) {
        List<SearchParam<User>> searchParams = SearchParam.create(params);
        Page<User> users = userService.getUsers(
                searchParams,
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort))
        );
        List<UserSessionDTO> dtos = users.getContent().stream()
                .map(UserSessionDTO::fromUser)
                .toList();
        CustomPage<UserSessionDTO> result = new CustomPage<>(dtos,users.getTotalPages(),users.getNumber() + 1);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{email}")
    public ResponseEntity<UserSessionDTO> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(UserSessionDTO.fromUser(user));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<UserSessionDTO> createUser(@RequestBody UserForm user) {
        User savedUser = userService.saveUser(user.toUser());
        return ResponseEntity.ok(UserSessionDTO.fromUser(savedUser));
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserSessionDTO> updateUser(@PathVariable String email, @RequestBody UserForm user) {
        User updatedUser = userService.updateUser(user.toUser(), email);
        return ResponseEntity.ok(UserSessionDTO.fromUser(updatedUser));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        Long id = userService.getUserByEmail(email).getId();
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
