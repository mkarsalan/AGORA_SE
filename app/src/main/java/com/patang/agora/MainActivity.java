package com.patang.agora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button4 = (Button) findViewById(R.id.button4);
        boolean isss = button4.isPressed();

        while (button4.isPressed()) {
            Toast.makeText(getApplicationContext(), Boolean.toString(isss), Toast.LENGTH_SHORT).show();
        }

//        System.out.println("HELLO WORLD");

    }
}
