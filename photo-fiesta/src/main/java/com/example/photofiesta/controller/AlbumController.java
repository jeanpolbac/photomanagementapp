package com.example.photofiesta.controller;

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
            return new  ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @PostMapping("/albums/")
    public ResponseEntity<?> createUserAlbum(@RequestBody Album albumObject) {
        Album album = albumService.createAlbum(albumObject);
        if (album != null) {
            message.put("message", "success, album created");
            message.put("data", album);
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

}
