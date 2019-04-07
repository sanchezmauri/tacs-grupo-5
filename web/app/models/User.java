package models;

import play.mvc.PathBindable;
import repos.UserRepository;

import java.time.LocalDateTime;
import java.util.function.Function;

public class User implements PathBindable<User> {
    private Long id;
    private String name;
    // todo: placeLists
    private LocalDateTime lastAccess;

    // este ctor está porque el pathBindable necesita una instancia
    // para hacer el bindeo path -> objeto
    public User(Long id, String name) {
        this.id = id;
        this.name = name;
        this.lastAccess = LocalDateTime.now();
    }

    public User() {
        id = 0L;
        name = "";
        lastAccess = LocalDateTime.now();
    }


    public Long getId() { return id; }
    public String getName() {
        return name;
    }

    public int listsCount() { return 1; }
    public int placesCount(Function<Object, Boolean> predicate) {
        // todo: filtrar lugares de listas con predicado
        return 10;
    }

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