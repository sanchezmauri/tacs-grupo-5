package models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import json.LocalDateTimeSerializer;
import org.mindrot.jbcrypt.BCrypt;
import play.mvc.PathBindable;
import repos.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class User implements PathBindable<User> {

    public enum Rol{
        ROOT,SYSUSER
    }


    private Rol rol;
    private Long id;
    private String name;
    private String email;
    private String passwordHash;

    private List<VenueList> venueslists;
    private LocalDateTime lastAccess;

    public User(Long id, String name, String email, String plaintextPassword, Rol rol) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.passwordHash = BCrypt.hashpw(plaintextPassword, BCrypt.gensalt());
        this.lastAccess = LocalDateTime.now();
        this.rol = rol;
        this.venueslists = new ArrayList<>();
    }

    // este ctor está porque el pathBindable necesita una instancia
    // para hacer el bindeo path -> objeto
    public User() {
        this(0L, "", "", "password", Rol.SYSUSER);
    }


    public Long getId() { return id; }
    public String getName() {
        return name;
    }
    public Rol getRol() {
        return rol;
    }
    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean checkPassword(String plaintextPassword) {
        return BCrypt.checkpw(plaintextPassword, passwordHash);
    }

    // venue lists methods
    public List<VenueList> getAllLists() {
        return venueslists;
    }

    public Optional<VenueList> getList(Long listId) {
        return venueslists.stream()
                .filter(list -> list.getId().equals(listId))
                .findFirst();
    }

    public void addList(VenueList newList) {
        venueslists.add(newList);
    }

    public boolean removeList(Long id) {
        return venueslists.removeIf(list -> list.getId().equals(id));
    }

    public void addVenueToList(VenueList list, Long venueId, String venueName) {
        if (!list.hasVenue(venueId)) {
            Optional<UserVenue> existingVenue = getVenue(venueId);

            list.addVenue(
                existingVenue.orElse(new UserVenue(venueId, venueName, false))
            );
        }
    }

    public Optional<UserVenue> getVenue(Long venueId) {
        return venueslists.stream()
                .flatMap(list -> list.getVenues().stream())
                .filter(venue -> venue.getId().equals(venueId))
                .findAny();
    }

    public int listsCount() { return venueslists.size(); }

    public int placesCount(Predicate<UserVenue> predicate) {
        return venueslists.stream()
                .flatMap(venueList -> venueList.getVenues().stream())
                .filter(predicate)
                .collect(Collectors.toSet())
                .size();
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