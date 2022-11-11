package com.misiontic.grupo4.securityBackend.services;

import com.misiontic.grupo4.securityBackend.models.Permission;
import com.misiontic.grupo4.securityBackend.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
/**
 *
 */
public class PermissionServices {

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Permission> index() {return (List<Permission>)this.permissionRepository.findAll();}

    public Optional<Permission> show(int id){
        return this.permissionRepository.findById(id);
    }

    public Permission create(Permission newPermission){
        if(newPermission.getId() == null){
            if(newPermission.getMethod() != null && newPermission.getUrl() != null && newPermission.getUrl() !=null)
                return this.permissionRepository.save(newPermission);
            else{
                // TODO 400 bad request
                return newPermission;
            }
        }
        else{
            // TODO validate if id exists
            return newPermission;
        }
    }

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
                // TODO 404 Not Found
                return updatePermission;
            }
        }
        else{
            // TODO 400 bas requestr, i >= 0
            return updatePermission;
        }
    }

    public boolean delete(int id){
        Boolean success = this.show(id).map(permission -> {
            this.permissionRepository.delete(permission);
            return true;
        }).orElse(false);
        return success;
    }

}
