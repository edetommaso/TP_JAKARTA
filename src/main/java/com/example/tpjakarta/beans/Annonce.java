package com.example.tpjakarta.beans;

import com.example.tpjakarta.utils.AnnonceStatus;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Table(name = "annonce")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String title;

    @Column(length = 256, nullable = false)
    private String description;

    @Column(length = 64, nullable = false)
    private String adress;

    @Column(length = 64, nullable = false)
    private String mail;

    private Timestamp date;

    private AnnonceStatus status;

    @ManyToOne
    private User author;

    @ManyToOne
    private Category category;

}