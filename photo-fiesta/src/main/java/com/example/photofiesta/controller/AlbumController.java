package com.example.photofiesta.controller;

import com.example.photofiesta.models.Album;
import com.example.photofiesta.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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

}
