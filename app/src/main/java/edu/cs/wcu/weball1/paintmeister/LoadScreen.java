package edu.cs.wcu.weball1.paintmeister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadScreen extends AppCompatActivity implements MyAdapter.ItemWasClicked {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<String> fileNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);

        // Get all of the file names
        this.fileNames = new ArrayList<>();
        File[] files = this.getFilesDir().listFiles();

        if (files != null) {
            for (File file: files) {
                this.fileNames.add(file.toString());
            }
        }

        // Set up recycler view
        this.recyclerView = findViewById(R.id.file_name_view);
        this.myAdapter = new MyAdapter(this.fileNames, this);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setAdapter(this.myAdapter);
    }

    @Override
    public void itemClicked(String text) {
        Intent paintScreen = new Intent(this, PaintActivity.class);
        paintScreen.putExtra("loadedFile", text);
        this.startActivity(paintScreen);
    }
}
