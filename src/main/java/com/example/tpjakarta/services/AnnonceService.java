package com.example.tpjakarta.services;

import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.AnnonceRepository;
import com.example.tpjakarta.utils.AnnonceStatus;

public class AnnonceService {

    private final AnnonceRepository annonceRepository;

    public AnnonceService(AnnonceRepository annonceRepository) {
        this.annonceRepository = annonceRepository;
    }

    /**
     * Publishes an announcement.
     * A user can only publish their own announcement.
     * @param annonce The announcement to publish.
     * @param currentUser The user attempting to publish the announcement.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean publishAnnonce(Annonce annonce, User currentUser) {
        if (annonce == null || currentUser == null || !annonce.getAuthor().getId().equals(currentUser.getId())) {
            // Business rule: only the author can publish their ad.
            return false;
        }
        annonce.setStatus(AnnonceStatus.PUBLISHED);
        annonceRepository.update(annonce);
        return true;
    }
}
