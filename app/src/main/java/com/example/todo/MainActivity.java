package com.example.todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    List<String> items;
    TextView tv;
    Button add;
    RecyclerView rv;
    ListAdaptor la;

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_EXIT_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.AddButton);
        tv = findViewById(R.id.EditText);
        rv = findViewById(R.id.RecyclerView);

        Objects.requireNonNull(getSupportActionBar()).setTitle("To Do List");

        load();
        ListAdaptor.OnLongClickListener longClick = new ListAdaptor.OnLongClickListener() {
            @Override
            public void OnItemLongClicked(int position) {
                items.remove(position);
                la.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item removed", Toast.LENGTH_SHORT).show();
                save();
            }
        };

        ListAdaptor.OnClickListener click = new ListAdaptor.OnClickListener() {
            @Override
            public void OnItemClicked(int position) {
                Log.e("MainActivity", "CLicked at "+position);
                //first create the activity
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                //pass date
                intent.putExtra(KEY_ITEM_POSITION, position);
                intent.putExtra(KEY_ITEM_TEXT, items.get(position));
                //switch to EditActivity
                startActivityForResult(intent, EDIT_EXIT_CODE);
            }
        };
        la = new ListAdaptor(items, longClick, click);
        rv.setAdapter(la);
        rv.setLayoutManager(new LinearLayoutManager(this));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newItem = tv.getText().toString();
                items.add(newItem);
                la.notifyItemInserted(items.size() - 1);
                tv.setText("");
                Toast.makeText(getApplicationContext(), "New item added", Toast.LENGTH_SHORT).show();
                save();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == EDIT_EXIT_CODE && resultCode == RESULT_OK) {
            //retrieve edited text and edited position and update
            assert intent != null;
            items.set(Objects.requireNonNull(intent.getExtras()).getInt(KEY_ITEM_POSITION), intent.getStringExtra(KEY_ITEM_TEXT));
            //notify the adaptor
            la.notifyItemChanged(intent.getExtras().getInt(KEY_ITEM_POSITION));
            //save the changes, tip for users
            save();
            Toast.makeText(getApplicationContext(), "Item edited.", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown Call to onActivityResult");
        }

    }

    private File getData() {
        return new File(getFilesDir(), "data.txt");
    }

    private void load() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getData(), Charset.defaultCharset()));
        } catch (IOException exception) {
            Log.e("MainActivity", "Error:", exception);
        }
    }

    private void save() {
        try {
            FileUtils.writeLines(getData(), items);
        } catch (IOException exception) {
            Log.e("MainActivity", "Error:", exception);
        }
    }
}