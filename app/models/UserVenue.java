package models;

import com.fasterxml.jackson.annotation.JsonGetter;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;

@Entity
public class UserVenue {
    // no uso herencia porque estas venues serían compartidas
    // entre todos los usuarios, para saber desde cuando está agregada en el sistema

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (obj.getClass() == UserVenue.class) {
            return id.equals(((UserVenue) obj).id);
        }
        return false;
    }

    @Id
    String id;
    @Reference
    private FoursquareVenue foursquare;
    private boolean visited;

    public UserVenue() { }

    public UserVenue(FoursquareVenue foursquareVenue, boolean visited) {
        this.foursquare = foursquareVenue;
        this.id = foursquareVenue.getId();
        this.visited = visited;
    }

    @JsonGetter("visited")
    public boolean wasVisited() { return visited; }

    public void visit() { visited = true; }

    // todo: esto lo hago rapido, no se como serializar bien con jackson
    public String getId() { return foursquare.getId(); }
    public String getName() { return foursquare.getName(); }
    public String getAddress() { return foursquare.getAddress(); }
    public FoursquareVenue getFoursquareVenue() { return foursquare; }

    public void setId(String id) { foursquare.setId(id); }
    public void setName(String name) { foursquare.setName(name); }
    public void setAddress(String address) { foursquare.setAddress(address); }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("UserVenue (")
                .append(id)
                .append(") visited? ")
                .append(visited)
                .append('\n')
                .append(foursquare.toString())
                .toString();
    }
}

