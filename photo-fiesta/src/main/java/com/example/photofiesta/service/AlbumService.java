package com.example.photofiesta.service;

import com.example.photofiesta.exception.InformationExistException;
import com.example.photofiesta.exception.InformationNotFoundException;
import com.example.photofiesta.models.Album;
import com.example.photofiesta.models.Photo;
import com.example.photofiesta.models.User;
import com.example.photofiesta.repository.AlbumRepository;
import com.example.photofiesta.repository.PhotoRepository;
import com.example.photofiesta.repository.UserRepository;
import com.example.photofiesta.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository, UserRepository userRepository, PhotoRepository photoRepository) {
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get albums belonging to the current logged-in user
     *
     * @return List of user's albums
     * @throws InformationNotFoundException if no albums are found
     */
    public List<Album> getUserAlbums() {
        List<Album> albumList = albumRepository.findByUserId(AlbumService.getCurrentLoggedInUser().getId());
        if (albumList.isEmpty()) {
            throw new InformationNotFoundException("No albums found for user ID " + AlbumService.getCurrentLoggedInUser().getId());
        }
        return albumList;
    }

    /**
     * Create a new album
     *
     * @param album The album to create
     * @return The created album
     */
    public Album createAlbum(Album album) {
        album.setUser(AlbumService.getCurrentLoggedInUser());
        return albumRepository.save(album);
    }

    /**
     * Delete an album by ID
     *
     * @param albumId The ID of the album to delete
     * @return A message confirming the deletion
     * @throws InformationNotFoundException if the album is not found
     */
    public Optional<Album> deleteAlbum (Long albumId) {
        Optional<Album> albumOptional = Optional.ofNullable(albumRepository.findByIdAndUserId(albumId, getCurrentLoggedInUser().getId()));
        if (albumOptional.isPresent()) {
            albumOptional.get().setUser(null);
            albumRepository.save(albumOptional.get());

            //ToDo WHY IS THIS NOT WORKING????
            albumRepository.deleteById(albumOptional.get().getId());
            return albumOptional;
        } else {
            throw new InformationNotFoundException("Album ID " + albumId + " does not belong to the current user!");
        }
    }




    /**
     * Get the currently logged-in user
     *
     * @return The current user
     */
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
            photoObject.setAlbum(albumRepository.findByIdAndUserId(getCurrentLoggedInUser().getAlbumList().get(0).getId(), getCurrentLoggedInUser().getId()));
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
