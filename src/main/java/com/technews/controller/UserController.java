package com.technews.controller;

import com.technews.model.Post;
import com.technews.model.User;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

@Autowired
    UserRepository repository;

@Autowired
    VoteRepository VoteRepository;

@GetMapping("/api/users")
    public List<User> getAllUser() {
    List<User> userList = repository.findAll();
    for (User u : userList) {
       List<Post> postList = u.getPosts();
       for (Post p : postList){
           p.setVoteCount(VoteRepository.countVotesByPostId(p.getId()));
       }
    }
    return userList;
}

@GetMapping("/api/users/{id}")
    public User getUserById(@PathVariable Integer id) {
    User returnUser = repository.getById(id);
    List<Post> postList = returnUser.getPosts();
    for (Post p : postList) {
        p.setVoteCount(VoteRepository.countVotesByPostId(p.getId()));
    }

    return returnUser;
}

@PostMapping("/api/user")
    public User addUser(@RequestBody User user) {
    // Encrypt password
    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
    repository.save(user);
    return user;
}

@DeleteMapping("/api/user/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
    repository.deleteById(id);
}
}
