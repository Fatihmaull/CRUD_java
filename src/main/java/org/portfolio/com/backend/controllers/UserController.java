package org.portfolio.com.backend.controllers;

import org.modelmapper.ModelMapper;
import org.portfolio.com.backend.dto.UserDto;
import org.portfolio.com.backend.model.User;
import org.portfolio.com.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Convert Entity -> DTO
    private UserDto convert(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    // Convert DTO -> Entity
    private User convertToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    // ✅ Default Load: Tampilkan semua data user
    @GetMapping(value = "/listall")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> dtoList = userRepository.findAll()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    // ✅ Create user
    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.saveAndFlush(convertToUser(userDto));
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // ✅ Update user
    @PutMapping(value = "/update")
    public ResponseEntity<?> update(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || userDto.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userRepository.saveAndFlush(convertToUser(userDto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // ✅ Delete user by ID
    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> delete(@RequestParam(name = "iduser") Long iduser) {
        if (userRepository.existsById(iduser)) {
            userRepository.deleteById(iduser);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("User doesn't exist", HttpStatus.BAD_REQUEST);
    }

    // ✅ Search user by ID
    @GetMapping(value = "/searchuser")
    public ResponseEntity<?> searchUser(@RequestParam(name = "iduser") Long iduser) {
        return userRepository.findById(iduser)
                .map(user -> new ResponseEntity<>(convert(user), HttpStatus.OK))
                .orElse(new ResponseEntity<>("User doesn't exist", HttpStatus.BAD_REQUEST));
    }

    // ✅ Search user by Name
    @GetMapping(value = "/searchbyname")
    public ResponseEntity<?> searchByName(@RequestParam(name = "name") String name) {
        List<User> users = userRepository.searchByName(name.trim().toUpperCase());

        if (users != null && !users.isEmpty()) {
            List<UserDto> dtoList = users.stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        }

        return new ResponseEntity<>("User doesn't exist", HttpStatus.BAD_REQUEST);
    }
}
