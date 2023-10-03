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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Optional<Album> deleteAlbum(Long albumId) {
        Optional<Album> albumOptional = Optional.ofNullable(albumRepository.findByIdAndUserId(albumId, getCurrentLoggedInUser().getId()));
        if (albumOptional.isPresent()) {
            albumOptional.get().setUser(null);
            albumRepository.save(albumOptional.get());

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
     * Creates and associates a photo with an album for the current logged-in user.
     *
     * @param albumId The ID of the album to which the photo should be associated.
     * @param photoObject The photo object containing the image URL and other details.
     * @return A map containing the created photo and a message describing the operation.
     * @throws InformationExistException if a photo with the same image URL already exists in the specified album.
     */
    public Map<String, Object> createAlbumPhoto(Long albumId, Photo photoObject) {
        Map<String, Object> response = new HashMap<>();
        Album album = albumRepository.findByIdAndUserId(albumId, getCurrentLoggedInUser().getId());
        String message;
        if (album == null) {
            photoObject.setAlbum(albumRepository.findByIdAndUserId(getCurrentLoggedInUser().getAlbumList().get(0).getId(), getCurrentLoggedInUser().getId()));
            message = "Photo added to the default album because the specified album does not exist.";
        } else {
            photoObject.setAlbum(album);
            message = "Photo added to the specified album.";
        }
        Photo photo = photoRepository.findByImageUrlAndAlbumId(photoObject.getImageUrl(), photoObject.getAlbum().getId());
        if (photo != null) {
            throw new InformationExistException("A photo with the image url " + photoObject.getImageUrl() + " already exists!");
        }
        response.put("photo", photoRepository.save(photoObject));
        response.put("message", message);
        return response;
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


    /**
     * Retrieves a list of photos belonging to the specified album ID for the current logged-in user.
     *
     * @param albumId The unique identifier of the album.
     * @return A list of photo objects associated with the specified album ID.
     * @throws InformationNotFoundException If the album with the given ID doesn't exist.
     */
    public List<Photo> getAlbumPhotos(Long albumId) {
        Optional<Album> optionalAlbum = Optional.ofNullable(albumRepository.findByIdAndUserId(albumId, getCurrentLoggedInUser().getId()));
        if (optionalAlbum.isPresent()) {
            return optionalAlbum.get().getPhotoList();
        } else {
            throw new InformationNotFoundException("album with id: " + albumId + " doesn't exist");
        }
    }

    /**
     * Retrieves a list of photos belonging to the specified album ID for the current logged-in user.
     *
     * @param albumId The unique identifier of the album.
     * @return A list of photo objects associated with the specified album ID.
     * @throws InformationNotFoundException If the album with the given ID doesn't exist.
     */
    public Photo getAlbumPhoto(Long albumId, Long photoId) {
        Optional<Album> albumOptional = Optional.ofNullable(albumRepository.findByIdAndUserId(albumId, getCurrentLoggedInUser().getId()));
        if (albumOptional.isPresent()) {
            Optional<Photo> photoOptional = photoRepository.findByAlbumId(albumId).stream().filter(p -> p.getId().equals(photoId)).findFirst();
            if (photoOptional.isEmpty()) {
                throw new InformationNotFoundException("photo with id: " + photoId + "in this album does not exist");
            } else {
                return photoOptional.get();
            }
        } else {
            throw new InformationNotFoundException("album with id: " + albumId + " doesn't exist");
        }
    }
}
