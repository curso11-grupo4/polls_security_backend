package com.misiontic.grupo4.securityBackend.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "permission")
public class Permission implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPermission;
    private String url;
    private String method;

    public Integer getId() {
        return idPermission;
    }

    public void setId(Integer id) {
        this.idPermission = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
