package com.example.photofiesta.service;

import com.example.photofiesta.exception.InformationExistException;
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

    public Photo createAlbumPhoto(Long albumId, Photo photoObject) {
        Album album = albumRepository.findByIdAndUserId(albumId,getCurrentLoggedInUser().getId());
        Photo photo = photoRepository.findByImageUrlAndAlbumId(photoObject.getImageUrl(),album.getId());
        if(photo != null){
            throw new InformationExistException("A photo with the image url " + photoObject.getImageUrl() + " already exists!");
        }
        photoObject.setAlbum(album);
        return photoRepository.save(photoObject);
    }

    public Photo updateAlbumPhoto(Long albumId, Long photoId, Photo photoObject){
        Optional<Album> albumOptional = Optional.of(albumRepository.findByIdAndUserId(albumId, getCurrentLoggedInUser().getId()));
        if(albumOptional.isPresent()){
            Optional<Photo> photoOptional = photoRepository.findByAlbumId(albumId).stream().filter(photo -> photo.getId().equals(photoId)).findFirst();
            if(photoOptional.isEmpty()){
                throw new InformationNotFoundException("A photo with id " + photoId + " is not found");
            } else {
                Photo existingPhoto = photoOptional.get();
                existingPhoto.setName(photoObject.getName());
                existingPhoto.setDescription(photoObject.getDescription());
                existingPhoto.setImageUrl(photoObject.getImageUrl());
                return photoRepository.save(existingPhoto);
                }
        } else {
            throw new InformationNotFoundException("An album with id " + albumId + " does not exist.");
        }
    }

    public Optional<Photo> deleteAlbumPhoto(Long albumId, Long photoId){
        Optional<Album> albumOptional = Optional.of(albumRepository.findByIdAndUserId(albumId, getCurrentLoggedInUser().getId()));
        if(albumOptional.isPresent()){
            Optional<Photo> photoOptional = photoRepository.findByAlbumId(albumId).stream().filter(photo -> photo.getId().equals(photoId)).findFirst();
            if(photoOptional.isEmpty()){
                throw new InformationNotFoundException("Photo with id " + photoId + " does not exist.");
            }
            photoRepository.deleteById(photoOptional.get().getId());
            return photoOptional;
        }
        throw new InformationNotFoundException("An album with id " + " does not exist.");
    }

}
