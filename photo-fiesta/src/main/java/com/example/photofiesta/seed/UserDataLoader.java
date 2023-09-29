package com.example.photofiesta.seed;

import com.example.photofiesta.models.User;
import com.example.photofiesta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserDataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        loadUserData();
    }

    private void loadUserData(){
        if(userRepository.count() == 0){
            User user1 = new User(null, "JohnDoe", "john.doe@example.com", "hashed_password123");
            User user2 = new User(null, "JaneDoe", "jane.doe@example.com", "hashed_password456");
            userRepository.save(user1);
            userRepository.save(user2);
        }
    }


}
