package com.example.tpjakarta.api.resource;

import com.example.tpjakarta.api.dto.AnnonceCreateDTO;
import com.example.tpjakarta.api.dto.AnnonceDTO;
import com.example.tpjakarta.api.security.UserPrincipal;
import com.example.tpjakarta.services.AnnonceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.tpjakarta.api.security.CustomUserDetails;
import jakarta.validation.Valid;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.example.tpjakarta.utils.AnnonceStatus;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/annonces")
public class AnnonceResource {

    private final AnnonceService annonceService;

    @Autowired
    public AnnonceResource(AnnonceService annonceService) {
        this.annonceService = annonceService;
    }

    private Long getAuthenticatedUserId(CustomUserDetails userDetails) {
        if (userDetails != null) {
            return userDetails.getUserId();
        }
        return null;
    }

    @GetMapping
    public ResponseEntity<Page<AnnonceDTO>> getAll(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "status", required = false) AnnonceStatus status,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "authorId", required = false) Long authorId,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            @RequestParam(value = "sort", defaultValue = "date,desc") String sortRaw,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        // Handling Timestamp conversions
        Timestamp fromTs = fromDate != null ? new Timestamp(fromDate.getTime()) : null;
        Timestamp toTs = toDate != null ? new Timestamp(toDate.getTime()) : null;

        Page<AnnonceDTO> result = annonceService.searchDynamic(q, status, categoryId, authorId, fromTs, toTs, sortRaw, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnonceDTO> getById(@PathVariable("id") Long id) {
        AnnonceDTO annonce = annonceService.findById(id);
        return ResponseEntity.ok(annonce);
    }

    @PostMapping
    public ResponseEntity<AnnonceDTO> create(@Valid @RequestBody AnnonceCreateDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = getAuthenticatedUserId(userDetails);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AnnonceDTO created = annonceService.create(dto, userId);
        return ResponseEntity.created(URI.create("/api/annonces/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnonceDTO> update(@PathVariable("id") Long id, @Valid @RequestBody AnnonceCreateDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = getAuthenticatedUserId(userDetails);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AnnonceDTO updated = annonceService.update(id, dto, userId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = getAuthenticatedUserId(userDetails);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        annonceService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AnnonceDTO> patch(@PathVariable("id") Long id, @RequestBody AnnonceCreateDTO updates, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = getAuthenticatedUserId(userDetails);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AnnonceDTO updated = annonceService.patch(id, updates, userId);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/archive")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> archive(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = getAuthenticatedUserId(userDetails);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        annonceService.archive(id, userId);
        return ResponseEntity.noContent().build();
    }
}
