package com.example.tpjakarta.api.resource;

import com.example.tpjakarta.api.dto.AnnonceCreateDTO;
import com.example.tpjakarta.api.dto.AnnonceDTO;
import com.example.tpjakarta.api.security.UserPrincipal;
import com.example.tpjakarta.services.AnnonceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/annonces")
public class AnnonceResource {

    private final AnnonceService annonceService;

    @Autowired
    public AnnonceResource(AnnonceService annonceService) {
        this.annonceService = annonceService;
    }

    private Long getAuthenticatedUserId(Principal principal) {
        if (principal != null) {
            // Spring Security might populate Principal as a UsernamePasswordAuthenticationToken
            // or we might have UserPrincipal custom context if filter sets it.
            // But normally, principal.getName() returns the username or id String.
            // Based on previous code in UserPrincipal, name is the string version.
            if (principal instanceof UserPrincipal) {
                return ((UserPrincipal) principal).getUserId();
            }
            try {
                return Long.parseLong(principal.getName());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @GetMapping
    public ResponseEntity<List<AnnonceDTO>> getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<AnnonceDTO> annonces = annonceService.findAll(page, size);
        return ResponseEntity.ok(annonces);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnonceDTO> getById(@PathVariable("id") Long id) {
        AnnonceDTO annonce = annonceService.findById(id);
        return ResponseEntity.ok(annonce);
    }

    @PostMapping
    public ResponseEntity<AnnonceDTO> create(@Valid @RequestBody AnnonceCreateDTO dto, Principal principal) {
        Long userId = getAuthenticatedUserId(principal);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AnnonceDTO created = annonceService.create(dto, userId);
        return ResponseEntity.created(URI.create("/api/annonces/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnonceDTO> update(@PathVariable("id") Long id, @Valid @RequestBody AnnonceCreateDTO dto, Principal principal) {
        Long userId = getAuthenticatedUserId(principal);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AnnonceDTO updated = annonceService.update(id, dto, userId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, Principal principal) {
        Long userId = getAuthenticatedUserId(principal);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        annonceService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AnnonceDTO> patch(@PathVariable("id") Long id, @RequestBody AnnonceCreateDTO updates, Principal principal) {
        Long userId = getAuthenticatedUserId(principal);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AnnonceDTO updated = annonceService.patch(id, updates, userId);
        return ResponseEntity.ok(updated);
    }
}
