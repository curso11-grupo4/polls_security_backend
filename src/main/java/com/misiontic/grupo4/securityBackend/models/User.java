package com.misiontic.grupo4.securityBackend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;
    @Column(name= "nickname", nullable = false, unique = true)
    private String nickname;
    @Column(name= "email", nullable = false, unique = true)
    private String email;
    @Column(name="password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name="IdRol")
    @JsonIgnoreProperties("users")
    private Rol rol;

    /**
     * This method returns the current user id
     * @return user id
     */
    public Integer getId() {
        return idUser;
    }

    /**
     * This method returns the current user nickname
     * @return user nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * This method updates the value for the user nickname
     * @param nickname user nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * This method returns the current user email
     * @return user email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This method returns the current encrypted user password
     * @return user password
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method updates the value of the user password
     * @param password user password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This method returns the current user rol
     * @return user rol
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * This method updates the value of the user rol
     * @param rol user rol
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
