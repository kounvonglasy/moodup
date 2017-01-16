package isep.moodup;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;


public class IncidentListAdapter extends ArrayAdapter<Incident> {

    private List<Incident> items;
    private int layoutResourceId;
    private Context context;

    public IncidentListAdapter(Context context, int layoutResourceId, List<Incident> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        IncidentHolder holder;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new IncidentHolder();
        holder.incident = items.get(position);
        holder.addGetIncidentButton = (Button) row.findViewById(R.id.incident);
        holder.addGetIncidentButton.setTag(holder.incident);
        holder.addLikeButton = (ImageButton) row.findViewById(R.id.like);
        holder.addLikeButton.setTag(holder.incident);
        holder.id = holder.incident.getId();
        holder.creationDate = (TextView) row.findViewById(R.id.creationDate);
        holder.title = (TextView) row.findViewById(R.id.title);
        holder.description = (TextView) row.findViewById(R.id.description);
        holder.duration =  (TextView) row.findViewById(R.id.duration);
        holder.login = (TextView) row.findViewById(R.id.userLogin);
        holder.nbLike = (TextView) row.findViewById(R.id.nbLike);
        // Session class instance
        SessionManager session = new SessionManager(this.context);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // login
        String login = user.get(Config.KEY_USER_LOGIN);
        //Hide the button from other users
        if(!holder.incident.getUserLogin().equals(login)) {
            View b = row.findViewById(R.id.incident);
            b.setVisibility(View.GONE);
        }
        row.setTag(holder);
        setupItem(holder);
        return row;
    }

    private void setupItem(IncidentHolder holder) {
        holder.title.setText("Title: " + holder.incident.getTitle());
        holder.description.setText("Description: " + holder.incident.getDescription());
        holder.creationDate.setText("Creation Date: " + holder.incident.getCreationDate());
        holder.login.setText("User login: "+ holder.incident.getUserLogin());
        holder.duration.setText("Duration: "+ holder.incident.getDuration() +" min");
        holder.nbLike.setText("Likes: "+ holder.incident.getNbLike());
    }

    public static class IncidentHolder {
        Incident incident;
        String id;
        TextView creationDate;
        TextView title;
        TextView description;
        TextView duration;
        TextView login;
        TextView nbLike;
        ImageButton addLikeButton;
        Button addGetIncidentButton;
    }

}