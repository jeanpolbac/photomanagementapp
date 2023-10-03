package com.example.photofiesta.seed;

import com.example.photofiesta.models.Album;
import com.example.photofiesta.models.Photo;
import com.example.photofiesta.models.User;
import com.example.photofiesta.repository.AlbumRepository;
import com.example.photofiesta.repository.PhotoRepository;
import com.example.photofiesta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final PhotoRepository photoRepository;

    private final AlbumRepository albumRepository;


    @Autowired
    public UserDataLoader(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder,
                          PhotoRepository photoRepository, AlbumRepository albumRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.photoRepository = photoRepository;
        this.albumRepository = albumRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadUserData();
    }

    private void loadUserData(){
        if(userRepository.count() == 0){
            User user1 = new User();
            user1.setUserName("Johnny");
            user1.setEmailAddress("john.doe@photofiesta.com");
            user1.setPassword(passwordEncoder.encode("password123"));
            if (user1.getAlbumList() == null) {
                user1.setAlbumList(new ArrayList<>());
            }
            userRepository.save(user1);

            Album defaultAlbum = new Album();
            defaultAlbum.setName(user1.getUserName() + "'s Album");
            defaultAlbum.setDescription("My default album");
            defaultAlbum.setUser(user1);

            albumRepository.save(defaultAlbum);

            user1.getAlbumList().add(defaultAlbum);
            userRepository.save(user1);

            Album secondAlbum = new Album();
            secondAlbum.setName(user1.getUserName() + "'s Favorite's Album");
            secondAlbum.setDescription("My favorite photos!");
            secondAlbum.setUser(user1);
            albumRepository.save(secondAlbum);
            user1.getAlbumList().add(secondAlbum);

            Album thirdAlbum = new Album();
            thirdAlbum.setName(user1.getUserName() + "'s Vacation Album");
            thirdAlbum.setDescription("My vacation photos!");
            thirdAlbum.setUser(user1);
            albumRepository.save(thirdAlbum);
            user1.getAlbumList().add(thirdAlbum);

            Photo photo1 = new Photo(null,
                    "Selfie",
                    "A beautiful Face",
                    "http://example.com/johnnys_first_selfie.jpg",
                    user1.getAlbumList().get(0)
            );
            photoRepository.save(photo1);
            Photo photo2 = new Photo(null,
                    "Taco's!!!",
                    "BEST TACO EVER!!!",
                    "http://example.com/taco.jpg",
                    user1.getAlbumList().get(1)
            );
            photoRepository.save(photo2);
            Photo photo3 = new Photo(null,
                    "Myrtle Beach",
                    "A hot day at the beach",
                    "http://example.com/myrtle_beach.jpg",
                    user1.getAlbumList().get(2)
            );
            photoRepository.save(photo3);
        }
    }


}
