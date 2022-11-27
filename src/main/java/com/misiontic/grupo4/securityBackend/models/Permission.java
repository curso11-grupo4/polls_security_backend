package com.misiontic.grupo4.securityBackend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "permission")
public class Permission implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPermission;
    @Column(name="url", nullable = false, unique = true)
    private String url;
    @Column(name = "method", nullable = false)
    private String method;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnoreProperties("permissions")
    private Set<Rol> roles;

    /**
     * This method returns the current permission id
     * @return permission id
     */
    public Integer getId() {
        return idPermission;
    }


    public void setId(Integer id) {
        this.idPermission = id;
    }

    /**
     * This method return the current permission url
     * @return permission url
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method updates the value of the permission url
     * @param url permission url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * This method returns the current permission method
     * @return permission method
     */
    public String getMethod() {
        return method;
    }

    /**
     * This method updates the value of the permission method
     * @param method permission method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     *
     * @return
     */
    public Set<Rol> getRoles() {
        return roles;
    }

    /**
     * This method returns the roles associated to the current permission
     * @return roles with current permission
     */
    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}
