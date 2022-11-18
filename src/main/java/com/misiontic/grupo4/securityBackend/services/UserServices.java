package com.misiontic.grupo4.securityBackend.services;

import com.misiontic.grupo4.securityBackend.repositories.UserRepository;
import com.misiontic.grupo4.securityBackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.module.ResolutionException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
/**
 *
 */
public class UserServices {
    @Autowired
    private UserRepository userRepository;

    public List<User> index(){
        return (List<User>)this.userRepository.findAll();
    }

    public Optional<User> show(int id){
        return userRepository.findById(id);
    }

    public Optional<User> showByNickname(String nickname){
        return this.userRepository.findByNickname(nickname);
    }

    public Optional<User> showByEmail(String email){
        return this.userRepository.findByEmail(email);
    }

    public User create(User newUser){
        if(newUser.getId() == null){
            if(newUser.getEmail() != null && newUser.getNickname() != null && newUser.getPassword() !=null) {
                newUser.setPassword(this.convertToSHA256(newUser.getPassword()));
                return this.userRepository.save(newUser);
            }
            else{
                // TODO 400 bad request
                return newUser;
            }
        }
        else{
            // TODO validate if id exists
            return newUser;
        }
    }

    public User update(int id, User updateUser){
        if(id > 0){
            Optional<User> tempUser = this.show(id);
            if(tempUser.isPresent()){
                if(updateUser.getNickname() != null)
                    tempUser.get().setNickname(updateUser.getNickname());
                if(updateUser.getPassword() != null)
                    tempUser.get().setPassword(this.convertToSHA256(updateUser.getPassword()));
                return this.userRepository.save(tempUser.get());
            }
            else{
                // TODO bad request
                return updateUser;
            }
        }
        else{
            // TODO 400 BAD REQUEST
            return updateUser;
        }
    }

    public boolean delete(int id){
        Boolean success = this.show(id).map(user -> {
            this.userRepository.delete(user);
            return true;
        }).orElse(false);
        return success;
    }

    public ResponseEntity<User> login(User user){
        User response;
        if(user.getPassword() != null && user.getEmail() != null) {
            String email = user.getEmail();
            String password = this.convertToSHA256(user.getPassword());
            Optional<User> result = this.userRepository.login(email, password);
            if(result.isEmpty())
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid access");
            else
                response = result.get();
        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mandatory fields had to been sent");
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    public String convertToSHA256(String password){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for(byte b: hash)
            sb.append( String.format("%02x", b));
        return sb.toString();
    }
}
