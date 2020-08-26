package com.example.myapp.parkme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParkListAdapter extends ArrayAdapter<JSONObject> {
    private final Context context;
    private final ArrayList<JSONObject> values;

    public ParkListAdapter(Context context, ArrayList<JSONObject> values) {
        super(context, R.layout.row_park_fragment, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_park_fragment, parent, false);
        JSONObject data = values.get(position);
        TextView rowDescription = (TextView)rowView.findViewById(R.id.history_row_description_edit);
        TextView rowCarid = (TextView)rowView.findViewById(R.id.history_row_carId_edit);
        TextView rowDriver = (TextView)rowView.findViewById(R.id.history_row_driver_edit);
        TextView rowLocation = (TextView)rowView.findViewById(R.id.history_row_location_edit);
        TextView rowTime = (TextView)rowView.findViewById(R.id.history_row_time_edit);
        try {
            String m = data.getString(Helper.TablePark.COLUMN_NAME_DESCRIPTION);
            rowDescription.setText(data.getString(Helper.TablePark.COLUMN_NAME_DESCRIPTION));
            String l = data.getString(Helper.TablePark.COLUMN_NAME_CAR_ID);
            rowCarid.setText(data.getString(Helper.TablePark.COLUMN_NAME_CAR_ID));
            rowDriver.setText(data.getString(Helper.TablePark.COLUMN_NAME_DRIVER_NAME));
            rowLocation.setText(data.getString(Helper.TablePark.COLUMN_NAME_PARK_NAME));
            rowTime.setText(data.getString(Helper.TablePark.COLUMN_NAME_TIMESTAMP));
            //imageView.setImageResource(R.drawable.action_history);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rowView;
    }
}