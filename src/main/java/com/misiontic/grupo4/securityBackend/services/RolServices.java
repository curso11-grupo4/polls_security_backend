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

    /**
     * Get all roles
     * @return a list of Rol object
     */
    public List<Rol> index(){
        return (List<Rol>)this.rolRepository.findAll();
    }

    /**
     * Get a specific Rol object by the id if this exists -Optional-
     *
     * @param id
     * @return an object with the information of a specific Rol
     */
    public Optional<Rol> show(int id){
        return this.rolRepository.findById(id);
    }

    /**
     * Create a new Rol, it must not come without id and
     * Rol
     * @param newRol
     * @return a Rol model
     */
    public Rol create(Rol newRol){
        if(newRol.getIdRol() == null){
            if(newRol.getName() != null)
                return this.rolRepository.save(newRol);
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Rol name is mandatory");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Rol.id is already in the database");
        }

    }

    /**
     * With the id the object is fetched and update
     * @param id
     * @param updateRol
     * @return a Rol model
     */
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
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Rol.id does not exist in database");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Rol.id need to be up to 0");
        }
    }

    /**
     * Rol is deleted by the id and if it is not found or
     * something come wrong, it will return false
     * @param id
     * @return a true or false dependig on the result
     */
    public boolean delete(int id){
        Boolean success = this.show(id).map(Rol -> {
            this.rolRepository.delete(Rol);
            return true;
        }).orElse(false);
        return success;
    }

    /**
     * a permission is joined with a rol
     * @param idRol
     * @param idPermission
     * @return if the rol does not have a permission yet, the permission is update
     */
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

    /**
     * Check if a specific rol has a specific permission
     * @param idRol
     * @param permission
     * @return if rol has the permission return true, else false
     */
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
