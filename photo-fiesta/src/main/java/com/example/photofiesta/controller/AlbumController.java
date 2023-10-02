package com.example.photofiesta.controller;

import com.example.photofiesta.exception.InformationNotFoundException;
import com.example.photofiesta.models.Album;
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
}


