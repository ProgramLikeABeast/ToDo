package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

    public class EditActivity extends AppCompatActivity {

        EditText tv;
        Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        tv=findViewById(R.id.UpdateText);
        edit=findViewById(R.id.EditButton);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit an item");

        tv.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create an intent which contains the results
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                //pass the data
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, tv.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, Objects.requireNonNull(getIntent().getExtras()).getInt(MainActivity.KEY_ITEM_POSITION));
                //set result of the intent
                setResult(RESULT_OK, intent);
                //finish the program
                finish();
            }
        });

    }

}