package com.example.photofiesta.service;

import com.example.photofiesta.exception.InformationExistException;
import com.example.photofiesta.models.Album;
import com.example.photofiesta.models.User;
import com.example.photofiesta.models.request.LoginRequest;
import com.example.photofiesta.repository.UserRepository;
import com.example.photofiesta.security.JWTUtils;
import com.example.photofiesta.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtils jwtUtils;

    private final AuthenticationManager authenticationManager;


    @Autowired
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, JWTUtils jwtUtils, @Lazy AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }
    /**
     * Create a new user in the system.
     *
     * @param userObject The User object to be created.
     * @return The created User object.
     * @throws InformationExistException if the user's email address already exists in the system.
     */
    public User createUser(User userObject) {
        if (!userRepository.existsByEmailAddress(userObject.getEmailAddress())) {
            userObject.setPassword(passwordEncoder.encode((userObject.getPassword())));
            if (userObject.getAlbumList() == null) {
                userObject.setAlbumList(new ArrayList<>());
            }
            Album defaultAlbum = new Album();
            defaultAlbum.setName(userObject.getUserName() + "'s Album");
            defaultAlbum.setUser(userObject);
            userObject.getAlbumList().add(defaultAlbum);
            userRepository.save(userObject);
            return userObject;
        } else {
            throw new InformationExistException("user email address " + userObject.getEmailAddress() + " already exists");
        }
    }

    /**
     * Login a user and generate a JWT token upon successful authentication.
     *
     * @param loginRequest The login request containing user credentials.
     * @return An Optional containing the JWT token if authentication is successful, otherwise empty.
     */
    public Optional<String> loginUser(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmailAddress(), loginRequest.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            return Optional.of(jwtUtils.generateJwtToken(myUserDetails));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public User findByUserEmailAddress(String emailAddress) {
        return userRepository.findUserByEmailAddress(emailAddress);
    }
}
