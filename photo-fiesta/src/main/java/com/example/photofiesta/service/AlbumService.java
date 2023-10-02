package com.example.photofiesta.service;

import com.example.photofiesta.exception.InformationNotFoundException;
import com.example.photofiesta.models.Album;
import com.example.photofiesta.models.User;
import com.example.photofiesta.repository.AlbumRepository;
import com.example.photofiesta.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
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
        return albumRepository.save(album);
    }

    /**
     * Delete an album by ID
     *
     * @param albumId The ID of the album to delete
     * @return A message confirming the deletion
     * @throws InformationNotFoundException if the album is not found
     */
    public String deleteAlbum (Long albumId) {
        Optional<Album> album = albumRepository.findById(albumId);
        if (album.isPresent()) {
            albumRepository.deleteById(albumId);
            return "Album deleted" ;
        } else {
            throw new InformationNotFoundException("Album ID " + albumId + " not found!");
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
}
