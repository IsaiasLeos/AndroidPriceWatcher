package cs4330.cs.utep.pricewatcher.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cs4330.cs.utep.pricewatcher.view.PriceFinder;
import cs4330.cs.utep.pricewatcher.model.Product;
import cs4330.cs.utep.pricewatcher.R;

public class MainActivity extends AppCompatActivity {


    Product product = new Product("https://www.amazon.com/LG-V35-ThinQ-Alexa-Hands-Free/dp/B07D46BMYT", "LG V35", 399.99, 0.00, 399.99);
    TextView name;
    TextView initial;
    TextView current;
    TextView change;
    TextView url;
    Button refresh;
    Button openWeb;

    /**
     * Initializes buttons and TextViews of the application.
     * Also handling when a user shares text to the application.
     *
     * @param savedInstanceState instance save
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleShare(getIntent()); //User shared a random link to the application

        name = findViewById(R.id.nameString);
        initial = findViewById(R.id.initialDouble);
        current = findViewById(R.id.currentDouble);
        change = findViewById(R.id.changeDouble);
        url = findViewById(R.id.urlString);
        openWeb = findViewById(R.id.openWeb);

        name.setText(product.getName());
        initial.setText(String.valueOf(product.getInitialPrice()));
        current.setText(String.valueOf(product.getCurrentPrice()));
        change.setText(String.valueOf(product.getChange()));
        url.setText(product.getURL());

        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener((view) -> onRefresh());
        openWeb.setOnClickListener((view) -> onClickProduct());
    }

    /**
     * Changes the price of the current product.
     * Currently pseudo-random.
     */
    protected void onRefresh() {
        PriceFinder findPrice = new PriceFinder();//Generate random price
        product.checkPrice(findPrice.getPrice(product.getInitialPrice()));//Checks the current price against the initial price
        current.setText(String.valueOf(product.getCurrentPrice()));//Sets the current price
        String changeString;
        //Checks the value of change to set the color green or red
        if (product.getChange() > 0.0) {
            changeString = "-" + Math.abs(product.getChange());
            this.change.setTextColor(Color.GREEN);
        } else {
            changeString = "+" + Math.abs(product.getChange());
            this.change.setTextColor(Color.RED);
        }
        this.change.setText(changeString);
    }

    /**
     * Currently handling the action of an application sharing text to this app.
     */
    private void handleShare(Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            }
        }
    }

    /**
     * Handles incoming shared text.
     *
     * @param intent shared intent
     */
    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            product.setSharedURL(sharedText);
            Toast.makeText(this, sharedText, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Opens browser activity given the current product URL.
     */
    protected void onClickProduct() {
        Intent launchBrowser = new Intent(getBaseContext(), BrowserActivity.class);
        launchBrowser.putExtra("url", product.getURL());
        startActivity(launchBrowser);
    }
}
