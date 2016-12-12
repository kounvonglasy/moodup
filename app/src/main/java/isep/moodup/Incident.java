package isep.moodup;

/**
 * Created by Kevin on 12/12/2016.
 */

import java.io.Serializable;

public class Incident implements Serializable {
    private static final long serialVersionUID = -5435670920302756945L;

    private String title;
    private String description;
    private String creationDate;
    private String id;

    public Incident(String id, String title, String description, String creationDate) {
        this.setId(id);
        this.setTitle(title);
        this.setDescription(description);
        this.setCreationDate(creationDate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
