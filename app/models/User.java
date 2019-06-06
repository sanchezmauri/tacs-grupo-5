package models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import json.LocalDateTimeSerializer;
import org.mindrot.jbcrypt.BCrypt;
import play.mvc.PathBindable;
import services.UsersService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Entity("users")
public class User implements PathBindable<User> {



    private Rol rol;
    @Id
    private String id;
    private String name;
    private String email;
    private String passwordHash;
    @Embedded
    private List<VenueList> venueslists;
    private LocalDateTime lastAccess;

    public User(String name, String email, String plaintextPassword, Rol rol) {
        this.email = email;
        this.name = name;
        this.passwordHash = BCrypt.hashpw(plaintextPassword, BCrypt.gensalt());
        this.lastAccess = LocalDateTime.now();
        this.rol = rol;
        this.venueslists = new ArrayList<>();
    }

    // este ctor está porque el pathBindable necesita una instancia
    // para hacer el bindeo path -> objeto
    public User(){
    }


    public String  getId() { return id; }
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

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<VenueList> getVenueslists() {
        return venueslists;
    }

    public void setVenueslists(List<VenueList> venueslists) {
        this.venueslists = venueslists;
    }

    public void setLastAccess(LocalDateTime lastAccess) {
        this.lastAccess = lastAccess;
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
        if (this.venueslists == null)
        {
            venueslists = new ArrayList<>();
        }
        venueslists.add(newList);
    }

    public boolean removeList(Long id) {
        return venueslists.removeIf(list -> list.getId().equals(id));
    }

    public int listsCount() { return venueslists.size(); }

    public void addVenueToList(VenueList list, String venueId, String venueName, String venueAddress) {
        // todo: buscar en un repositorio de lugares foursquare a ver si ya existe,
        // esto para cumplir el requerimiento de agregados desde
        if (!list.hasVenue(venueId)) {
            Optional<UserVenue> existingVenue = getVenue(venueId);

            list.addVenue(
                existingVenue.orElse(
                    new UserVenue(
                        new FoursquareVenue(venueId, venueName, venueAddress, LocalDate.now()),
                        false)
                )
            );
        }
    }

    public Optional<UserVenue> getVenue(String venueId) {
        return venueslists.stream()
                .map(list -> list.getVenue(venueId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny();
    }

    public boolean hasVenue(String venueId) {
        return getVenue(venueId).isPresent();
    }

    public int venuesCount(Predicate<UserVenue> predicate) {
        return venueslists.stream()
                .flatMap(venueList -> venueList.getVenues().stream())
                .filter(predicate)
                .collect(Collectors.toSet())
                .size();
    }
    // venues list methods end


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime getLastAccess() { return lastAccess; }


    // estos metodos son para que linkee una id de la request path
    // con un user (interface PathBindable)
    public User bind(String key, String txt) {
        return Optional.ofNullable(UsersService.findById(txt)).orElseThrow(() -> new RuntimeException("Couldn't find user with id " + txt));
    }

    public String unbind(String key) {
        return id.toString();
    }
    public String javascriptUnbind() { return null; }

    @Override
    public String toString() {
        return this.name + " (" + this.email +")";
    }
}