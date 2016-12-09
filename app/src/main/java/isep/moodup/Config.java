package isep.moodup;

/**
 * Created by Kevin on 03/12/2016.
 */

public class Config {
    //Address of our scripts of the CRUD
    //en local => http://10.0.2.2:8888/addIncident
    public static final String URL_ADD_INCIDENT = "http://young-journey-48135.herokuapp.com/addIncident";
    //en local => http://10.0.2.2:8888/getAllIncidents
    public static final String URL_GET_ALL_INCIDENTS = "http://young-journey-48135.herokuapp.com/getAllIncidents";
    //en local => http://10.0.2.2:8888/getAllUsers
    public static final String URL_GET_ALL_USERS = "http://young-journey-48135.herokuapp.com/getAllUsers";
    //en local => http://10.0.2.2:8888/getAllSeverites
    public static final String URL_GET_ALL_SEVERITES = "http://young-journey-48135.herokuapp.com/getAllSeverites";
    //en local => http://10.0.2.2:8888/getAllTypes
    public static final String URL_GET_ALL_TYPES = "http://young-journey-48135.herokuapp.com/getAllTypes";
    //en local => http://10.0.2.2:8888/getIncident?id=
    public static final String URL_GET_INCIDENT = "http://young-journey-48135.herokuapp.com/getIncident?id=";
    //en local => http://10.0.2.2:8888/updateIncident
    public static final String URL_UPDATE_INCIDENT = "http://young-journey-48135.herokuapp.com/updateIncident";
    //en local => http://10.0.2.2:8888/deleteIncident?id=
    public static final String URL_DELETE_INCIDENT = "http://young-journey-48135.herokuapp.com/deleteIncident?id=";

    //Keys that will be used to send the request to the server
    public static final String KEY_INCIDENT_ID = "idIncident";
    public static final String KEY_INCIDENT_TITLE = "title";
    public static final String KEY_INCIDENT_DESCRIPTION = "description";
    public static final String KEY_INCIDENT_USER_NAME = "userName";
    public static final String KEY_INCIDENT_SEVERITE_NAME = "severiteName";
    public static final String KEY_INCIDENT_TYPE_NAME = "typeName";
    public static final String KEY_INCIDENT_DURATION = "duration";

    //JSON tags
    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_INCIDENT_ID = "id";
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
