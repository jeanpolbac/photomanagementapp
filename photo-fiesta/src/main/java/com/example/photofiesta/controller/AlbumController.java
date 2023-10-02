package com.example.photofiesta.controller;

import com.example.photofiesta.exception.InformationNotFoundException;
import com.example.photofiesta.models.Album;
import com.example.photofiesta.models.Photo;
import com.example.photofiesta.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api")
public class AlbumController {

    @Autowired
    private AlbumService albumService;
    static HashMap<String, Object> message = new HashMap<>();

    @GetMapping("/albums/")
    public ResponseEntity<?> getUserAlbums() {
        List<Album> albumList = albumService.getUserAlbums();
        if (albumList.isEmpty()) {
            message.put("message", "Cannot find any albums");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            message.put("message", "Able to retrieve albums");
            message.put("data", albumList);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @PostMapping("/albums/")
    public ResponseEntity<?> createUserAlbum(@RequestBody Album albumObject) {
        Album albumToCreate = albumService.createAlbum(albumObject);
        if (albumToCreate != null) {
            message.put("message", "success, album created");
            message.put("data", albumToCreate);
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else {
            message.put("message", "unable to create album.");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @GetMapping("/albums/{albumId}/photos/")
    public ResponseEntity<?> getPhotos(@PathVariable(value = "albumId") Long albumId) {
        List<Photo> photoList = albumService.getAlbumPhotos(albumId);
        if (photoList.isEmpty()) {
            message.put("message", "cannot find photos in album");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            message.put("message", "success");
            message.put("data", photoList);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @GetMapping("/albums/{albumId}/photos/{photoId}/")
    public ResponseEntity<?> getAlbumPhoto(@PathVariable(value = "albumId") Long albumId, @PathVariable(value = "photoId") Long photoId) {
        Optional<Photo> photoOptional = Optional.ofNullable(albumService.getAlbumPhoto(albumId, photoId));
        if (photoOptional.isPresent()) {
            message.put("message", "success");
            message.put("data", photoOptional.get());
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            message.put("message", "cannot find photo in album");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/albums/{albumId}/")
    public ResponseEntity<?> deleteUserAlbum(@PathVariable(value = "albumId") Long albumId) {
        Optional<Album> albumToDelete = albumService.deleteAlbum(albumId);
        if (albumToDelete.isEmpty()) {
            message.put("message", "Cannot find Album with ID " + albumId);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            message.put("message", "Album with ID " + albumId + " has been successfully deleted");
            message.put("data", albumToDelete.get());
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @PostMapping("/albums/{albumId}/photos/")
    public ResponseEntity<?> createAlbumPhoto(@PathVariable(value = "albumId") Long albumId, @RequestBody Photo photoObject){
        Photo photo = albumService.createAlbumPhoto(albumId,photoObject);
        if(photo != null){
            message.put("message","success, photo added to selected album");
            message.put("data",photo);
            return new ResponseEntity<>(message,HttpStatus.CREATED);
        } else {
            message.put("message", "unable to add new photo");
            return new ResponseEntity<>(message,HttpStatus.OK);
        }
    }

    @PutMapping("/albums/{albumId}/photos/{photoId}")
    public ResponseEntity<?> updateAlbumPhoto(@PathVariable(value = "albumId") Long albumId, @PathVariable(value = "photoId") Long photoId,@RequestBody Photo photoObject){
        Optional<Photo> photoToUpdate = Optional.of(albumService.updateAlbumPhoto(albumId,photoId,photoObject));
        if(photoToUpdate.isEmpty()){
            message.put("message","Cannot find photo with id " + photoId + " to update");
            return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
        } else {
            message.put("message","photo with id " + photoId + " has been successfully updated");
            message.put("data",photoToUpdate.get());
            return new ResponseEntity<>(message,HttpStatus.OK);
        }
    }

    @DeleteMapping("/albums/{albumId}/photos/{photoId}/")
    public ResponseEntity<?> deleteAlbumPhoto(@PathVariable(value = "albumId") Long albumId, @PathVariable(value = "photoId") Long photoId){
        Optional<Photo> photoToDelete = albumService.deleteAlbumPhoto(albumId, photoId);
        if(photoToDelete.isEmpty()){
            message.put("message","cannot find photo with id " + photoId + " in album with id " + albumId);
            return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
        } else {
            message.put("message","photo with id " + photoId + " has been successfully deleted.");
            message.put("data",photoToDelete.get());
            return new ResponseEntity<>(message,HttpStatus.OK);
        }
    }
}

