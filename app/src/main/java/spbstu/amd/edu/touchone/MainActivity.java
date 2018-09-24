package spbstu.amd.edu.touchone;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button buttonLevelMenu;
    Button buttonAbout;
    Button buttonQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLevelMenu = (Button) findViewById(R.id.buttonLevelMenu);
        buttonAbout = (Button) findViewById(R.id.buttonAbout);
        buttonQuit = (Button) findViewById(R.id.buttonQuit);

        View.OnClickListener oclButtonLevel = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked level button", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LevelActivity.class);
                startActivity(intent);
            }
        };

        View.OnClickListener oclButtonAbout = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked about button", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        };

        View.OnClickListener oclButtonQuit = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked quit button", Toast.LENGTH_SHORT).show();
                System.exit(0);
            }
        };

        buttonLevelMenu.setOnClickListener(oclButtonLevel);
        buttonAbout.setOnClickListener(oclButtonAbout);
        buttonQuit.setOnClickListener(oclButtonQuit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
