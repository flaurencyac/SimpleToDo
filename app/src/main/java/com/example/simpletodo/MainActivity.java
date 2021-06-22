package com.example.simpletodo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // defined model class as a list of strings
    List<String> items;

    // create member variables
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    // onCreate is called when the activity is created, displays existing items on the model (list of string items)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this inflates the layout file from res/layout/activity_main.xml
        setContentView(R.layout.activity_main);

        // define member variables
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        // define instance of items adapter OnLongClickListener
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                items.remove(position);
                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                // Notify user that their item was successfully removed
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        // Construct an items adapter
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);

        // binds the newly created adapter and layout manager to rvItems, the Recycler View lst and data source
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager( this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // grab whatever the user typed into the edit text and assign that into a String var
                String todoItem = etItem.getText().toString();
                // Add new item to the model
                items.add(todoItem);
                // Notify adapter than an item is inserted
                itemsAdapter.notifyItemInserted(items.size()-1);
                // Clear edit text once added onto Recycler View
                etItem.setText("");
                // Create a "Toast", which simply tells the user that they've added their item successfully
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    // For persistence this returns file where we stored our list of to do items
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // This function will load items by reading every line of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            // log error into LogCat
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // This function saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}