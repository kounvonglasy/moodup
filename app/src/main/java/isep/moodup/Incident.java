package isep.moodup;
import java.io.Serializable;


public class Incident implements Serializable {
    private static final long serialVersionUID = -5435670920302756945L;

    private String title;
    private String description;
    private String creationDate;
    private String id;
    private String duration;
    private String userLogin;
    private String nbLike;
    private String severite;

    public Incident(String id, String title, String description, String creationDate, String duration, String userLogin, String nbLike, String severite) {
        this.setId(id);
        this.setTitle(title);
        this.setDescription(description);
        this.setCreationDate(creationDate);
        this.setUserLogin(userLogin);
        this.setDuration(duration);
        this.setNbLike(nbLike);
        this.setSeverite(severite);
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

    public String getSeverite() {
        return severite;
    }

    public void setSeverite (String severite) {
        this.severite = severite;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getNbLike() {
        return nbLike;
    }

    public void setNbLike(String nbLike) {
        this.nbLike = nbLike;
    }
}
