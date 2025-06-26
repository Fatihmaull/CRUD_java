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
import java.util.Optional;
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

    private UserDto convert(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User convertToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    @GetMapping(value = "/listall")
    public ResponseEntity<List<UserDto>> listUsers() {

        List<UserDto> dtoList = userRepository.findAll()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@Valid @RequestBody UserDto userDto, BindingResult bindingResul) {

        if (bindingResul.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.saveAndFlush(convertToUser(userDto));
        return new ResponseEntity<>(user,HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> update(@Valid @RequestBody UserDto userDto, BindingResult bindingResul) {

        if (bindingResul.hasErrors() || userDto.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userRepository.saveAndFlush(convertToUser(userDto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> delete(@RequestParam(name = "iduser") Long iduser) {

        if (userRepository.existsById(iduser)) {
            userRepository.deleteById(iduser);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("User don't exist", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/searchuser")
    public ResponseEntity<?> searchuser(@RequestParam(name = "iduser") Long iduser) {

        if (userRepository.existsById(iduser)) {
            User user = userRepository.findById(iduser).get();
            return new ResponseEntity<>(convert(user), HttpStatus.OK);
        }

        return new ResponseEntity<>("User don't exist", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/searchbyname")
    public ResponseEntity<?> searchByName(@RequestParam(name = "name") String name) {


        List<User> user = userRepository.searchByName(name.trim().toUpperCase());

        if(user != null) {
            return new ResponseEntity<>(user.stream()
                    .map(this::convert)
                    .collect(Collectors.toList()), HttpStatus.OK);
        }

        return new ResponseEntity<>("User don't exist", HttpStatus.BAD_REQUEST);
    }

}
