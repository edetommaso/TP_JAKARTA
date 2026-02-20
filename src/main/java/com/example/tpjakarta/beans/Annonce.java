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

    @Enumerated(EnumType.STRING)
    private AnnonceStatus status;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Version
    private Integer version;

}