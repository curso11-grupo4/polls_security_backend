package com.misiontic.grupo4.securityBackend.services;

import com.misiontic.grupo4.securityBackend.repositories.UserRepository;
import com.misiontic.grupo4.securityBackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public User create(User newUser){
        if(newUser.getId() == null){
            if(newUser.getEmail() != null && newUser.getNickname() != null && newUser.getPassword() !=null)
                return this.userRepository.save(newUser);
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
                    tempUser.get().setPassword(updateUser.getPassword());
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
}
