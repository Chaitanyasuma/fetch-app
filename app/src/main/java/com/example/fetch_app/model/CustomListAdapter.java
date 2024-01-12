package com.example.fetch_app.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fetch_app.R;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<Element> {

    public CustomListAdapter(Context context, ArrayList<Element> elements) {
        super(context, 0, elements);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Element item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_view, parent, false);
        }

        // Lookup view for data population
        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView listId = (TextView) convertView.findViewById(R.id.listID);
        TextView name = (TextView) convertView.findViewById(R.id.name);

        // Populate the data into the template view using the data object

        assert item != null;
        id.setText(String.valueOf(item.getId()));
        listId.setText(String.valueOf(item.getListId()));
        name.setText(item.getName());

        // Return the completed view to render on screen
        return convertView;

    }
}
