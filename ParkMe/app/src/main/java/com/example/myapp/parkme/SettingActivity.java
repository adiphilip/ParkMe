package com.example.myapp.parkme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    private SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setting = getSharedPreferences(Helper.PREFS_NAME, MODE_PRIVATE);
        setSetting();

    }
    @Override
    public void onPause(){
        super.onPause();
        try{
            saveSetting(this.findViewById(R.id.setting_save_button));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private void setSetting(){
        String driverName = setting.getString(Helper.PreferencesNames.DRIVER_NAME, "yossi choen");
        String carId = setting.getString(Helper.PreferencesNames.CAR_ID, "11-222-33");
        String description = setting.getString(Helper.PreferencesNames.DESCRIPTION, "my Honda");

        ((TextView)findViewById(R.id.setting_driver_name_edit)).setText(driverName);
        ((TextView)findViewById(R.id.setting_carID_edit)).setText(carId);
        ((TextView)findViewById(R.id.setting_description_edit)).setText(description);

    }

    public void saveSetting(View view){
        String driverName = ((TextView)findViewById(R.id.setting_driver_name_edit)).getText().toString();
        String carId = ((TextView)findViewById(R.id.setting_carID_edit)).getText().toString();
        String description = ((TextView)findViewById(R.id.setting_description_edit)).getText().toString();

        SharedPreferences.Editor editor = setting.edit();
        editor.putString(Helper.PreferencesNames.DRIVER_NAME, driverName);
        editor.putString(Helper.PreferencesNames.CAR_ID, carId);
        editor.putString(Helper.PreferencesNames.DESCRIPTION, description);
        editor.commit();

        Toast.makeText(view.getContext(), getString(R.string.setting_car_toast), Toast.LENGTH_SHORT).show();

    }




}
