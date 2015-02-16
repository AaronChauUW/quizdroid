package edu.washington.chau93.quizdroid;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.washington.chau93.quizdroid.domains.Topic;

/**
 * Created by Aaron Chau on 2/15/2015.
 */
public class CustomAdapter extends BaseAdapter implements View.OnClickListener {
    private Activity activity;
    private ArrayList<Topic> data;
    private static LayoutInflater inflater = null;
    public Resources res;
    private Topic tempTopic;

    // Constructor
    public CustomAdapter(Activity a, ArrayList<Topic> data, Resources res){
        this.activity = a;
        this.data = data;
        this.res = res;

        tempTopic = null;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    // Get the amount of topics we have.
    @Override
    public int getCount() {
        if(data.size()<=0)
            return 1;
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView topicTitle;
        public TextView shortD;
        public ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView = convertView;
        ViewHolder holder;

        if (convertView == null){
            // Inflate our custom list item view
            customView = inflater.inflate(R.layout.custom_list_item, null);

            // Use View Holder to hold the widgets
            holder = new ViewHolder();
            holder.topicTitle = (TextView) customView.findViewById(R.id.customListTopic);
            holder.shortD = (TextView) customView.findViewById(R.id.customListShortD);
            holder.img = (ImageView) customView.findViewById(R.id.customListImageView);

            // Set holder with Layout Inflater
            customView.setTag(holder);
        } else {
            holder = (ViewHolder) customView.getTag();
        }
        if(data.size() <= 0){
            holder.topicTitle.setText("There are no quiz topics.");
        } else {
            // Get topics from Array List
            tempTopic = (Topic) data.get(position);

            // Set values into widgets
            holder.topicTitle.setText(tempTopic.getTitle());
            holder.shortD.setText(tempTopic.getShortDescription());

            holder.img.setImageResource(
                    res.getIdentifier(
                            tempTopic.getImage(),
                            "drawable", activity.getPackageName()
                    )
            );
        }

        return customView;
    }

    @Override
    public void onClick(View v) {
        Log.d("Custom Adapter", "Row button clicked.");
    }
}
