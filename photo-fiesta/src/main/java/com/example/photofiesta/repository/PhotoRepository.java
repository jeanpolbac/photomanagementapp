package com.example.photofiesta.repository;

import com.example.photofiesta.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByAlbumId(Long albumId);
    Photo findByIdAndAlbumId(Long photoId, Long albumId);
    Photo findByImageUrlAndAlbumId(String imageUrl, Long albumId);
}
