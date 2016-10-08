package com.example.junjiefeng.javasucks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ListView tasklist;
    private EditText title;
    private EditText description;
    private ArrayList<HashMap<String, String>> list;
    private CheckBox checkBox;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        title = (EditText) findViewById(R.id.tasktitle);
        description = (EditText) findViewById(R.id.task_des);
        tasklist = (ListView) findViewById(R.id.task_list);
        list = new ArrayList<HashMap<String, String>>();

        adapter = new SimpleAdapter(this,
                list,
                R.layout.my_task_list,
                new String[]{"title", "des"},
                new int[]{R.id.Title, R.id.Des}) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                checkBox.setChecked(false);
                return view;
            }
        };
        long_click();
        load_file();
        tasklist.setAdapter(adapter);
    }

    public void onClick(View view){
        if(!("".equals(title.getText().toString().trim()))){
        if (!("".equals(description.getText().toString().trim()))) {

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("title", "Task title: " + title.getText().toString());
                    map.put("des", "Task description :" + description.getText().toString());
                    list.add(map);
                    tasklist.requestLayout();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "The task description should not be empty", Toast.LENGTH_SHORT).show();
                }
    }
        else{
            Toast.makeText(MainActivity.this, "The task title should not be empty", Toast.LENGTH_SHORT).show();
        }
        try {
            FileOutputStream fileOut = this.openFileOutput("tasklist.txt",
                    this.MODE_PRIVATE);
            // save data
            for(int i= 0; i<list.size();i++) {
                fileOut.write(list.get(i).get("title").getBytes());
                //Toast.makeText(MainActivity.this,list.get(1).get("title") ,Toast.LENGTH_SHORT).show();
                fileOut.write(",".getBytes());
                fileOut.write(list.get(i).get("des").getBytes());
                fileOut.write("\n".getBytes());
            }
            //
            fileOut.close();
            Toast.makeText(MainActivity.this, "List saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        title.setText("");
        description.setText("");
    }
    private void long_click(){
        tasklist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                list.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void load_file(){
        try{
            //
            FileInputStream fileInput = this.openFileInput("tasklist.txt");
            DataInputStream data = new DataInputStream(fileInput);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(data));
            String str = null;
            List<String> name = Arrays.asList(this.fileList());
            if (name.contains("tasklist.txt")) {
                Toast.makeText(MainActivity.this, "Loading previous list", Toast.LENGTH_SHORT).show();
            }
            while ((str = br.readLine()) != null) {
                String[] task = str.split(",");
                HashMap<String, String> map = new HashMap<>();
                map.put("title",  task[0]);
                map.put("des", task[1]);
                list.add(map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tasklist.setAdapter(adapter);
        tasklist.requestLayout();
    }

}