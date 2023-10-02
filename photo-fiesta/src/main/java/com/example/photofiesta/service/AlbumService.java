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

    /**
     * Creates a new photo and associates it with a specific album.
     *
     * @param albumId The ID of the album in which the photo will be added.
     * @param photoObject The photo object containing photo details.
     * @return The newly created photo object.
     * @throws InformationExistException If a photo with the same image URL already exists in the album.
     * @throws InformationNotFoundException If the specified album or user does not exist.
     */
    public Photo createAlbumPhoto(Long albumId, Photo photoObject) {
        Album album = albumRepository.findByIdAndUserId(albumId, getCurrentLoggedInUser().getId());
        if(album == null){
            photoObject.setAlbum(albumRepository.findByIdAndUserId(1L, getCurrentLoggedInUser().getId()));
        } else {
            photoObject.setAlbum(album);
        }
        Photo photo = photoRepository.findByImageUrlAndAlbumId(photoObject.getImageUrl(),photoObject.getAlbum().getId());
        if(photo != null){
            throw new InformationExistException("A photo with the image url " + photoObject.getImageUrl() + " already exists!");
        }
        return photoRepository.save(photoObject);
    }

    /**
     * Updates the details of a photo in a specific album.
     *
     * @param albumId The ID of the album containing the photo to be updated.
     * @param photoId The ID of the photo to be updated.
     * @param photoObject The updated photo object with new details.
     * @return The updated photo object.
     * @throws InformationNotFoundException If the specified album, photo, or album user does not exist.
     */
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

    /**
     * Deletes a photo from an album.
     *
     * @param albumId The ID of the album from which the photo will be deleted.
     * @param photoId The ID of the photo to be deleted.
     * @return An optional containing the deleted photo if successful, otherwise an empty optional.
     * @throws InformationNotFoundException If the specified album or photo does not exist.
     */
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
