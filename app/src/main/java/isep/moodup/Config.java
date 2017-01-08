package isep.moodup;

/**
 * Created by Kevin on 03/12/2016.
 */

public class Config {
    //Address of our scripts of the CRUD (for incidents)
    //en local => http://10.0.2.2:8888/addIncident
    public static final String URL_ADD_INCIDENT = "http://10.0.2.2:8888/addIncident";
    //en local => http://10.0.2.2:8888/getAllIncidents
    public static final String URL_GET_ALL_INCIDENTS = "http://10.0.2.2:8888/getAllIncidents";
    //en local => http://10.0.2.2:8888/getAllUsers
    public static final String URL_GET_ALL_USERS = "http://10.0.2.2:8888/getAllUsers";
    //en local => http://10.0.2.2:8888/getAllSeverites
    public static final String URL_GET_ALL_SEVERITES = "http://10.0.2.2:8888/getAllSeverites";
    //en local => http://10.0.2.2:8888/getAllTypes
    public static final String URL_GET_ALL_TYPES = "http://10.0.2.2:8888/getAllTypes";
    //en local => http://10.0.2.2:8888/getIncident?id=
    public static final String URL_GET_INCIDENT = "http://10.0.2.2:8888/getIncident?id=";
    //en local => http://10.0.2.2:8888/updateIncident
    public static final String URL_UPDATE_INCIDENT = "http://10.0.2.2:8888/updateIncident";
    //en local => http://10.0.2.2:8888/deleteIncident?id=
    public static final String URL_DELETE_INCIDENT = "http://10.0.2.2:8888/deleteIncident?id=";

    //Address for the likes
    public static final String URL_ADD_USER = "http://10.0.2.2:8888/addUser";
    //en local => http://10.0.2.2:8888/addLike
    public static final String URL_ADD_LIKE = "http://10.0.2.2:8888/addLike";
    //en local => http://10.0.2.2:8888/checkLike
    public static final String URL_CHECK_LIKE = "http://10.0.2.2:8888/checkLike";

    //Adress for the account
    //en local => http://10.0.2.2:8888/login
    public static final String LOGIN_URL = "http://10.0.2.2:8888/login";

    //Keys that will be used to send the request to the server
    //Incident
    public static final String KEY_INCIDENT_ID = "idIncident";
    public static final String KEY_INCIDENT_TITLE = "title";
    public static final String KEY_INCIDENT_DESCRIPTION = "description";
    public static final String KEY_INCIDENT_USER_NAME = "userName";
    public static final String KEY_INCIDENT_SEVERITE_NAME = "severiteName";
    public static final String KEY_INCIDENT_TYPE_NAME = "typeName";
    public static final String KEY_INCIDENT_DURATION = "duration";
    public static final String KEY_INCIDENT_ID_USER = "idUser";

    //User
    public static final String KEY_USERNAME_NAME = "name";
    public static final String KEY_USERNAME_FIRSTNAME = "firstName";
    public static final String KEY_USERNAME_EMAIL = "email";
    public static final String KEY_USERNAME_LOGIN = "login";
    public static final String KEY_USERNAME_PASSWORD = "password";
    public static final String KEY_USERNAME_PASSWORD_CONFIRM = "passwordConfirm";

    //JSON tags
    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_INCIDENT_TITLE = "title";
    public static final String TAG_INCIDENT_DESCRIPTION = "description";
    public static final String TAG_INCIDENT_USER = "userName";
    public static final String TAG_INCIDENT_SEVERITE = "severiteName";
    public static final String TAG_INCIDENT_TYPE = "typeName";
    public static final String TAG_INCIDENT_CREATION_DATE = "creationDate";
    public static final String TAG_INCIDENT_DURATION = "duration";

    //incident id to pass with intent
    public static final String INCIDENT_ID = "idIncident";

}
