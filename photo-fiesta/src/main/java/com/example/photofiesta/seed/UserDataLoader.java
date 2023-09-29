package com.example.photofiesta.seed;

import com.example.photofiesta.models.Album;
import com.example.photofiesta.models.User;
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

    @Autowired
    public UserDataLoader(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        loadUserData();
    }

    private void loadUserData(){
        if(userRepository.count() == 0){
            User user1 = new User();
            user1.setUserName("JohnDoe");
            user1.setEmailAddress("john.doe@example.com");
            user1.setPassword(passwordEncoder.encode("hashed_password123"));
            if (user1.getAlbumList() == null) {
                user1.setAlbumList(new ArrayList<>());
            }
            Album defaultAlbum = new Album();
            defaultAlbum.setName(user1.getUserName() + "'s Album");
            defaultAlbum.setUser(user1);
            user1.getAlbumList().add(defaultAlbum);
            userRepository.save(user1);
        }
    }


}
