package com.example.photofiesta.models;

import javax.persistence.*;

@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String imageUrl;
}
