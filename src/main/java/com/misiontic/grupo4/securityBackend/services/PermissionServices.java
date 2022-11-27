package com.misiontic.grupo4.securityBackend.services;

import com.misiontic.grupo4.securityBackend.models.Permission;
import com.misiontic.grupo4.securityBackend.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
/**
 *
 */
public class PermissionServices {

    @Autowired
    private PermissionRepository permissionRepository;

    /** Get all permissions
     * @return a list of Permission object
     */
    public List<Permission> index() {
        return (List<Permission>)this.permissionRepository.findAll();}

    /** Get a specific permission object by id if this exists -Optional-
     * @return an object with the information of a specific permission
     */
    public Optional<Permission> show(int id){
        return this.permissionRepository.findById(id);}

    /**
     * Create a new permission, it must not come without id and
     * URL and Method are mandatory
     * @param newPermission
     * @return a Permission model
     */
    public Permission create(Permission newPermission){
        if(newPermission.getId() == null){
            if(newPermission.getMethod() != null && newPermission.getUrl() != null && newPermission.getUrl() !=null)
                return this.permissionRepository.save(newPermission);
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Permission and method are mandatory");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Permission.id is already in the database");
        }
    }

    /**
     * With the id the object is brought and updated.
     * It checks is teh id exist.
     * @param id
     * @param updatePermission
     * @return a Permission model
     */
    public Permission update(int id, Permission updatePermission){
        if(id>0){
            Optional<Permission> tempPermission = this.show(id);
            if(tempPermission.isPresent()){
                if(updatePermission.getUrl() != null)
                    tempPermission.get().setUrl(updatePermission.getUrl());
                if(updatePermission.getMethod() != null)
                    tempPermission.get().setMethod(updatePermission.getMethod());
                return this.permissionRepository.save(tempPermission.get());
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "permission.id does not exist in database");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Permission.id need to be up to 0");
        }
    }

    /**
     * Permission is deleted by the id and if the id is not
     * found or something is wrong, it will return false
     * @param id
     * @return a true or false depending on the result
     */
    public boolean delete(int id){
        Boolean success = this.show(id).map(permission -> {
            this.permissionRepository.delete(permission);
            return true;
        }).orElse(false);
        return success;
    }

}
