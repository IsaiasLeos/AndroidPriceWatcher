package cs4330.cs.utep.mypricewatcher.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import cs4330.cs.utep.mypricewatcher.R;

public class ScrollingActivity extends AppCompatActivity {

    TextView name;
    TextView init;
    TextView currt;
    TextView change;
    TextView date;
    TextView url;

    /**
     * When the user clicks a textview, will paste its contents into the user's clipboard.
     *
     * @param name
     * @param v
     */
    public void toClipboard(String name, View v) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", name);
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
        Snackbar snackbar = Snackbar.make(v, "Name has been copied to system clipboard.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * Initializes the scrolling layout, creates the toolbar, and handles sharing to displaying
     * detail information about the user's product.
     *
     * @param savedInstanceState instance saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Details");
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        name.setOnClickListener(v -> toClipboard(nameString, v));
        url.setOnClickListener(v -> toClipboard(urlDetailed, v));
        name.setText(Html.fromHtml(String.format("<b>%s</b>",
                nameString)));
        init.setText(Html.fromHtml("Initial Price: " + String.format("<b>$%s</b>",
                initString)));
        currt.setText(Html.fromHtml("Current Price: " + String.format("<b>$%s</b>",
                currString)));
        change.setText(Html.fromHtml("Change: " + String.format("<b>%s</b>",
                changeString)));
        date.setText(Html.fromHtml("Date: " + String.format("<b>%s</b>",
                dateDetailed)));
        url.setText(Html.fromHtml("URL: " + String.format("<b>%s</b>",
                urlDetailed)));

    }
}
