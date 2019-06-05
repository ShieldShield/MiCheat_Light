package com.example.micheatlight.Activities;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.micheatlight.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static com.example.micheatlight.R.string.Cheat_list;
import static com.example.micheatlight.R.string.Share_list;
import static com.example.micheatlight.R.string.about_list;
import static com.example.micheatlight.R.string.converter_list;
import static com.example.micheatlight.R.string.snack_to_be_implementet_;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    private static final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.ListView);

        final ArrayList<String> arrayList=new ArrayList<>();

        //Liste benennen
        arrayList.add(getString(converter_list));
        arrayList.add(getString(Cheat_list));
        arrayList.add(getString(Share_list));
        arrayList.add("Settings");
        arrayList.add(getString(about_list));

        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        //Trennung
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar snackbar= (Snackbar) Snackbar.make(view,getString(snack_to_be_implementet_),Snackbar.LENGTH_LONG);
                if(position==0) {
                    //Converter
                    Intent getConverterScreenIntent = new Intent(MainActivity.this,
                            ConverterActivity.class);
                    getConverterScreenIntent.putExtra("CallingActivity","MainActivity");
                    startActivity(getConverterScreenIntent);
                } else if(position==1) {
                    //Cheat Service
                    snackbar.show();
                } else if(position==2) {
                    //Share
                    snackbar.show();
                } else if(position==3) {
                    //settings
                    snackbar.show();
                } else if(position==4) {
                    //About
                    snackbar.show();
                }
            }
        });
    }

}
