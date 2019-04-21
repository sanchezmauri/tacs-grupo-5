package models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import json.LocalDateTimeSerializer;
import org.mindrot.jbcrypt.BCrypt;
import play.mvc.PathBindable;
import repos.UserRepository;

import java.time.LocalDateTime;
import java.util.function.Function;

public class User implements PathBindable<User> {

    public enum Rol{
        ROOT,SYSUSER
    }


    private Rol rol;
    private Long id;
    private String name;

    private String passwordHash;

    // todo: placeLists
    private LocalDateTime lastAccess;

    public User(Long id, String name, String plaintextPassword, Rol rol) {
        this.id = id;
        this.name = name;
        this.passwordHash = BCrypt.hashpw(plaintextPassword, BCrypt.gensalt());
        this.lastAccess = LocalDateTime.now();
        this.rol = rol;
    }

    // este ctor está porque el pathBindable necesita una instancia
    // para hacer el bindeo path -> objeto
    public User() {
        id = 0L;
        name = "";
        lastAccess = LocalDateTime.now();
        passwordHash = "";
    }


    public Long getId() { return id; }
    public String getName() {
        return name;
    }

    public Rol getRol() {
        return rol;
    }

    public boolean checkPassword(String plaintextPassword) {
        return BCrypt.checkpw(plaintextPassword, passwordHash);
    }

    public int listsCount() { return 1; }
    public int placesCount(Function<Object, Boolean> predicate) {
        // todo: filtrar lugares de listas con predicado
        return 10;
    }

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime getLastAccess() { return lastAccess; }


    // estos metodos son para que linkee una id de la request path
    // con un user (interface PathBindable)
    public User bind(String key, String txt) {
        return UserRepository.find(Long.valueOf(txt))
            .orElseThrow(() -> new RuntimeException("Couldn't find user with id " + txt));
    }

    public String unbind(String key) {
        return id.toString();
    }
    public String javascriptUnbind() { return null; }
}