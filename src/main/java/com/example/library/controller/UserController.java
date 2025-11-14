package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import com.example.library.dto.LoginRequest;
import com.example.library.dto.LoginResponse;
import com.example.library.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserController(UserRepository repo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<User>> all() {
        try {
            List<User> users = repo.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve users");
        }
    }

    // Create user
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        try {
            if (repo.findByEmail(user.getEmail()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User saved = repo.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user data: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user");
        }
    }

    /**
     * Register endpoint - creates user and returns JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
            if (repo.findByEmail(user.getEmail()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User saved = repo.save(user);

            String token = jwtUtil.generateToken(saved.getId().toString(), saved.getEmail(), saved.getRole().name());

            LoginResponse response = new LoginResponse(
                    saved.getId(),
                    saved.getName(),
                    saved.getSurname(),
                    saved.getEmail(),
                    saved.getAddress(),
                    saved.getCity(),
                    saved.getRole(),
                    token
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user data: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to register user");
        }
    }

    /**
     * Login endpoint - validates credentials and returns user information with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            if (loginRequest.getEmail() == null || loginRequest.getEmail().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
            }

            Optional<User> userOpt = repo.findByEmail(loginRequest.getEmail());

            if (userOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
            }

            User user = userOpt.get();

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
            }

            String token = jwtUtil.generateToken(user.getId().toString(), user.getEmail(), user.getRole().name());

            LoginResponse response = new LoginResponse(
                    user.getId(),
                    user.getName(),
                    user.getSurname(),
                    user.getEmail(),
                    user.getAddress(),
                    user.getCity(),
                    user.getRole(),
                    token
            );

            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Login failed");
        }
    }

    // GET user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getOne(@PathVariable Integer id) {
        try {
            if (id == null || id <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user ID");
            }

            User user = repo.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            return ResponseEntity.ok(user);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve user");
        }
    }

    // update user
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id, @Valid @RequestBody User updated) {
        try {
            if (id == null || id <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user ID");
            }

            User user = repo.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            if (updated.getEmail() != null && !updated.getEmail().equals(user.getEmail())) {
                if (repo.findByEmail(updated.getEmail()).isPresent()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
                }
            }

            if (updated.getName() != null && !updated.getName().isBlank()) {
                user.setName(updated.getName());
            }
            if (updated.getSurname() != null && !updated.getSurname().isBlank()) {
                user.setSurname(updated.getSurname());
            }
            if (updated.getEmail() != null && !updated.getEmail().isBlank()) {
                user.setEmail(updated.getEmail());
            }
            if (updated.getAddress() != null && !updated.getAddress().isBlank()) {
                user.setAddress(updated.getAddress());
            }
            if (updated.getCity() != null && !updated.getCity().isBlank()) {
                user.setCity(updated.getCity());
            }

            if (updated.getPassword() != null && !updated.getPassword().isEmpty() && updated.getPassword().length() >= 6) {
                user.setPassword(passwordEncoder.encode(updated.getPassword()));
            }

            User saved = repo.save(user);
            return ResponseEntity.ok(saved);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user data: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update user");
        }
    }

    /**
     * Delete user by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            if (id == null || id <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user ID");
            }

            if (!repo.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete user");
        }
    }
}
