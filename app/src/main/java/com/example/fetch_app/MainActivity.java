package com.example.fetch_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fetch_app.model.CustomListAdapter;
import com.example.fetch_app.model.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String JSON_URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //new
        new FetchDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//        // Sample data
//        ArrayList<Element> elements = new ArrayList<>();
//        elements.add(new Element(1, 101, "Item 1"));
//        elements.add(new Element(2, 102, "Item 2"));
//        elements.add(new Element(3, 101, "Item 3"));
//        elements.add(new Element(4, 103, "Item 4"));
//        elements.add(new Element(5, 102, "Item 5"));

//        // Group elements by listId using TreeMap
//        Map<Integer, ArrayList<Element>> groupedElements = new TreeMap<>();
//        for (Element element : elements) {
//            int listId = element.getListId();
//            if (!groupedElements.containsKey(listId)) {
//                groupedElements.put(listId, new ArrayList<>());
//            }
//            groupedElements.get(listId).add(element);
//        }
//
//        // Sort elements within each group by name
//        for (ArrayList<Element> group : groupedElements.values()) {
//            Collections.sort(group, new Comparator<Element>() {
//                @Override
//                public int compare(Element e1, Element e2) {
//                    return e1.getName().compareToIgnoreCase(e2.getName());
//                }
//            });
//        }
//
//        // Flatten the sorted and grouped elements into a single list
//        ArrayList<Element> sortedAndGroupedElements = new ArrayList<>();
//        for (ArrayList<Element> group : groupedElements.values()) {
//            sortedAndGroupedElements.addAll(group);
//        }
//
//        CustomListAdapter adapter = new CustomListAdapter(this, sortedAndGroupedElements);
//
//        ListView listView = (ListView) findViewById(R.id.listView);
//
//        listView.setAdapter(adapter);
    }

    private class FetchDataTask extends AsyncTask<Void, Void, ArrayList<Element>> {
        @Override
        protected ArrayList<Element> doInBackground(Void... voids) {
            ArrayList<Element> elements = new ArrayList<>();

            try {
                URL url = new URL(JSON_URL);
                // open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    JSONArray jsonArray = new JSONArray(stringBuilder.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        int listId = jsonObject.getInt("listId");
                        String name = jsonObject.getString("name");

                        // Add a check for non-empty names
                        if (!name.isEmpty()) {
                            elements.add(new Element(id, listId, name));
                        }
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return elements;
        }

        @Override
        protected void onPostExecute(ArrayList<Element> elements) {
            // Group elements by listId using TreeMap
            Map<Integer, ArrayList<Element>> groupedElements = new TreeMap<>();
            for (Element element : elements) {
                int listId = element.getListId();
                if (!groupedElements.containsKey(listId)) {
                    groupedElements.put(listId, new ArrayList<>());
                }
                groupedElements.get(listId).add(element);
            }

            // Sort elements within each group by name
            for (ArrayList<Element> group : groupedElements.values()) {
                Collections.sort(group, new Comparator<Element>() {
                    @Override
                    public int compare(Element e1, Element e2) {
                        return e1.getName().compareToIgnoreCase(e2.getName());
                    }
                });
            }

            // Flatten the sorted and grouped elements into a single list
            ArrayList<Element> sortedAndGroupedElements = new ArrayList<>();
            for (ArrayList<Element> group : groupedElements.values()) {
                sortedAndGroupedElements.addAll(group);
            }

            CustomListAdapter adapter = new CustomListAdapter(MainActivity.this, sortedAndGroupedElements);

            ListView listView = (ListView) findViewById(R.id.listView);

            listView.setAdapter(adapter);
        }
    }
}