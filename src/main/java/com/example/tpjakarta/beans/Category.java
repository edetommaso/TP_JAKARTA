package com.example.tpjakarta.beans;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String label;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Annonce> annonces = new ArrayList<>();

}
