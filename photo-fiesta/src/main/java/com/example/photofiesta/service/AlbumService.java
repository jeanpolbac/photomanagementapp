package com.example.photofiesta.service;

import com.example.photofiesta.exception.InformationExistException;
import com.example.photofiesta.exception.InformationNotFoundException;
import com.example.photofiesta.models.Album;
import com.example.photofiesta.models.User;
import com.example.photofiesta.repository.AlbumRepository;
import com.example.photofiesta.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
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

    //TODO implement controller and then test
    public Album createAlbum(Album albumObject) {
        User currentLoggedInUser = getCurrentLoggedInUser();
        Album album = albumRepository.findByName(albumObject.getName());
        if (album != null) {
            throw new InformationExistException("album with name " + albumObject.getName() + " already exists");
        } else {
            albumObject.setUser(getCurrentLoggedInUser());
            return albumRepository.save(albumObject);
        }
    }
}
