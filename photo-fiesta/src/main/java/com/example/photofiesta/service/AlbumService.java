package com.example.photofiesta.service;

import com.example.photofiesta.exception.InformationNotFoundException;
import com.example.photofiesta.models.Album;
import com.example.photofiesta.models.Photo;
import com.example.photofiesta.models.User;
import com.example.photofiesta.repository.AlbumRepository;
import com.example.photofiesta.repository.PhotoRepository;
import com.example.photofiesta.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository, PhotoRepository photoRepository) {
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
    }

    public List<Album> getUserAlbums() {
        List<Album> albumList = albumRepository.findByUserId(AlbumService.getCurrentLoggedInUser().getId());
        if (albumList.isEmpty()) {
            throw new InformationNotFoundException("no albums found for user id " + AlbumService.getCurrentLoggedInUser().getId());
        }
        return albumList;
    }

    public Album createAlbum(Album album) {
        return albumRepository.save(album);
    }

    public static User getCurrentLoggedInUser() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }

    public List<Photo> getAlbumPhotos(Long albumId) {
        Optional<Album> optionalAlbum = Optional.ofNullable(albumRepository.findByIdAndUserId(albumId, getCurrentLoggedInUser().getId()));
        if (optionalAlbum.isPresent()) {
            return optionalAlbum.get().getPhotoList();
        } else {
            throw new InformationNotFoundException("album with id: " + albumId + " doesn't exist");
        }
    }
}
