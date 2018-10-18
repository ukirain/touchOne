package spbstu.amd.edu.touchone;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class LevelActivity extends AppCompatActivity {

    ActionBar actionBar;
    Button ibOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        ibOne = findViewById(R.id.ibOne);

        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        View.OnClickListener oclIbOne = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LevelActivity.this, "Clicked level button", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LevelActivity.this, GameActivity.class);
                startActivity(intent);
            }
        };

        ibOne.setOnClickListener(oclIbOne);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
