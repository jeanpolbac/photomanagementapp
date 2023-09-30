package com.example.photofiesta.controller;

import com.example.photofiesta.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class AlbumController {

    private AlbumService albumService;

    @GetMapping("/albums/")
    public ResponseEntity<?> getUserAlbums() {

        return ResponseEntity.ok("You have photo albums available");
    }



}
