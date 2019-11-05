package psoft.proj.backend.ajude.users.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psoft.proj.backend.ajude.users.entities.User;
import psoft.proj.backend.ajude.users.services.JwtService;
import psoft.proj.backend.ajude.users.services.UsersService;

import javax.servlet.ServletException;
import java.rmi.ServerException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {

    private UsersService usersService;
    private JwtService jwtService;

    public UsersController (UsersService usersService, JwtService jwtService) {
        super ();
        this.usersService = usersService;
        this.jwtService = jwtService;
    }

    @CrossOrigin
    @PostMapping("")
    public ResponseEntity<User> postUser (@RequestBody User user) {
        try {
            return new ResponseEntity<>(usersService.postUser(user), HttpStatus.CREATED);
        } catch (ServerException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin
    @GetMapping("")
    public ResponseEntity<List<User>> getUsers () {
        return new ResponseEntity<>(usersService.getUsers(), HttpStatus.OK);
    }

    @CrossOrigin
    //todo: achar um nome melhor pra essa rota
    @GetMapping("/auth")
    public ResponseEntity<User> getUserByHeader (@RequestHeader("Authorization") String header) {
        try {
            return new ResponseEntity<>(jwtService.getUserByHeader(header), HttpStatus.OK);
        } catch (ServletException e) {
            if (e.toString().equals("Token inexistente ou mal formatado!"))
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin
    @GetMapping("/{email}")
    public ResponseEntity<User> getUser (@PathVariable String email) {
        Optional<User> user = usersService.getUser(email);
        if (user.isPresent())
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @CrossOrigin
    @DeleteMapping("")
    public ResponseEntity deleteUsers () {
        usersService.deleteUsers();
        return new ResponseEntity(HttpStatus.OK);
    }
}
