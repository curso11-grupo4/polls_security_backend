package com.misiontic.grupo4.securityBackend.services;


import com.misiontic.grupo4.securityBackend.models.Permission;
import com.misiontic.grupo4.securityBackend.models.Rol;
import com.misiontic.grupo4.securityBackend.repositories.PermissionRepository;
import com.misiontic.grupo4.securityBackend.repositories.RolRepository;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RolServices {
    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Rol> index(){
        return (List<Rol>)this.rolRepository.findAll();
    }

    public Optional<Rol> show(int id){
        return this.rolRepository.findById(id);
    }

    public Rol create(Rol newRol){
        if(newRol.getIdRol() == null){
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

    public ResponseEntity<Rol> updateAddPermission(int idRol, int idPermission){
        Optional<Rol> rol = this.rolRepository.findById(idRol);
        if(rol.isPresent()){
            Optional<Permission> permission = this.permissionRepository.findById(idPermission);
            if(permission.isPresent()){
                Set<Permission> tempPermissions = rol.get().getPermissions();
                if(tempPermissions.contains(permission.get()))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol already has the permission");
                else{
                    tempPermissions.add(permission.get());
                    rol.get().setPermissions(tempPermissions);
                    return new ResponseEntity<>(this.rolRepository.save(rol.get()), HttpStatus.CREATED);
                }
            }
            else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission.id does not exist in data base");
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol.id does not exist in data base");
    }

    public ResponseEntity<Boolean> validateGrant(int idRol, Permission permission){
        boolean isGrant = false;
        Optional<Rol> rol = this.rolRepository.findById(idRol);
        if(rol.isPresent()){
            for(Permission rolPermission: rol.get().getPermissions()){
                if(rolPermission.getUrl().equals(permission.getUrl()) &&
                    rolPermission.getMethod().equals(permission.getMethod())){
                        isGrant = true;
                        break;
                }
            }
            if(isGrant)
                return new ResponseEntity<>(true, HttpStatus.OK);
            else
                return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol.id does not exist in data base");
    }

}
