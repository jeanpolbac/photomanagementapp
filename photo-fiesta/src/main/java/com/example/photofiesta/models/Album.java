package com.example.photofiesta.models;

import javax.persistence.*;

@Entity
@Table(name = "albums")
public class Album {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;



}
