package models;

public class UserVenue extends Venue{
    private boolean visited;

    public UserVenue(Long id, String name, boolean visited) {
        super(id, name);
        this.visited = visited;
    }

    public boolean wasVisited() { return visited; }

    public void visit() { visited = true; }
}

