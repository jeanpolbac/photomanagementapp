package com.example.photofiesta.seed;

import com.example.photofiesta.models.User;
import com.example.photofiesta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
            userRepository.save(user1);
            User user2 = new User(null, "JaneDoe", "jane.doe@example.com", "hashed_password456");
            userRepository.save(user2);
        }
    }


}
