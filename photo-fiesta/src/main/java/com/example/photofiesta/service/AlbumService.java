package com.example.photofiesta.service;

import com.example.photofiesta.models.Album;
import com.example.photofiesta.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public List<Album> getUserAlbums(Long userId) {
        return albumRepository.findByUserId(userId);
    }
}
