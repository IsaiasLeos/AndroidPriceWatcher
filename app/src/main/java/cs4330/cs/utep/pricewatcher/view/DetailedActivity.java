package cs4330.cs.utep.pricewatcher.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cs4330.cs.utep.pricewatcher.R;

public class DetailedActivity extends AppCompatActivity {

    TextView name;
    TextView init;
    TextView currt;
    TextView change;
    TextView date;
    TextView url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Intent intent = getIntent();
        name = findViewById(R.id.nameDetailed);
        init = findViewById(R.id.initialDetailed);
        currt = findViewById(R.id.currentDetailed);
        change = findViewById(R.id.changeDetailed);
        date = findViewById(R.id.dateDetailed);
        url = findViewById(R.id.urlDetailed);
        String nameString = intent.getStringExtra("name");
        Double initString = intent.getDoubleExtra("init", 0.00);
        Double currString = intent.getDoubleExtra("curr", 0.00);
        Double changeString = intent.getDoubleExtra("change", 0.00);
        String dateDetailed = intent.getStringExtra("date");
        String urlDetailed = intent.getStringExtra("url");
        name = findViewById(R.id.nameDetailed);
        name.setText(Html.fromHtml("Name: " + String.format("<b>%s</b><br>",
                nameString)));
        init = findViewById(R.id.initialDetailed);
        init.setText(Html.fromHtml("Initial Price: " + String.format("<b>$%s</b><br>",
                initString)));
        currt = findViewById(R.id.currentDetailed);
        currt.setText(Html.fromHtml("Current Price: " + String.format("<b>$%s</b><br>",
                currString)));
        change = findViewById(R.id.changeDetailed);
        change.setText(Html.fromHtml("Change: " + String.format("<b>%s</b><br>",
                changeString)));
        date = findViewById(R.id.dateDetailed);
        date.setText(Html.fromHtml("Date: " + String.format("<b>%s</b><br>",
                dateDetailed)));
        url = findViewById(R.id.urlDetailed);
        url.setText(Html.fromHtml("URL: " + String.format("<b>%s</b><br>",
                urlDetailed)));
    }
}
