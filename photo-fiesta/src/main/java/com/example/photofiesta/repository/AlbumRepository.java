package com.example.photofiesta.repository;

import com.example.photofiesta.models.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByUserId(Long userId);

    List<Album> findByName(String albumName);

}
