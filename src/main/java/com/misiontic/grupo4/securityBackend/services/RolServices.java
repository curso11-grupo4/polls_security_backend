package com.misiontic.grupo4.securityBackend.services;


import com.misiontic.grupo4.securityBackend.models.Rol;
import com.misiontic.grupo4.securityBackend.repositories.RolRepository;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolServices {
    @Autowired
    private RolRepository rolRepository;

    public List<Rol> index(){
        return (List<Rol>)this.rolRepository.findAll();
    }

    public Optional<Rol> show(int id){
        return this.rolRepository.findById(id);
    }

    public Rol create(Rol newRol){
        if(newRol.getId() == null){
            if(newRol.getName() != null)
                return this.rolRepository.save(newRol);
            else{
                // TODO 400 bad request
                return newRol;
            }
        }
        else{
            // TODO validate if exists, 4000 bad request
            return newRol;
        }

    }

    public Rol update(int id, Rol updateRol){
        if(id>0){
            Optional<Rol> tempRol = this.show(id);
            if(tempRol.isPresent()){
                if(updateRol.getName() != null)
                    tempRol.get().setName(updateRol.getName());
                if(updateRol.getDescription() != null)
                    tempRol.get().setDescription(updateRol.getDescription());
                return this.rolRepository.save(tempRol.get());
            }
            else{
                // TODO 404 bad request
                return updateRol;
            }
        }
        else{
            // TODO 400 bas request
            return updateRol;
        }
    }

    public boolean delete(int id){
        Boolean success = this.show(id).map(Rol -> {
            this.rolRepository.delete(Rol);
            return true;
        }).orElse(false);
        return success;
    }
}
