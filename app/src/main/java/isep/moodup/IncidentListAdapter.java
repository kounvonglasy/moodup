package isep.moodup;

/**
 * Created by Kevin on 12/12/2016.
 */

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
        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    private void setupItem(IncidentHolder holder) {
        holder.title.setText("Title: " + holder.incident.getTitle());
        holder.description.setText("Description: " + holder.incident.getDescription());
        holder.creationDate.setText("Creation Date: " + holder.incident.getCreationDate());
    }

    public static class IncidentHolder {
        Incident incident;
        String id;
        TextView creationDate;
        TextView title;
        TextView description;
        ImageButton addLikeButton;
        Button addGetIncidentButton;
    }

}