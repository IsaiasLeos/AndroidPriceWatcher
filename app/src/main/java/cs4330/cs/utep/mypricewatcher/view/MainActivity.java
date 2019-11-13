package cs4330.cs.utep.mypricewatcher.view;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cs4330.cs.utep.mypricewatcher.R;
import cs4330.cs.utep.mypricewatcher.controller.DatabaseHandler;
import cs4330.cs.utep.mypricewatcher.controller.PriceFinder;
import cs4330.cs.utep.mypricewatcher.model.AboutActivity;
import cs4330.cs.utep.mypricewatcher.model.ListAdapter;
import cs4330.cs.utep.mypricewatcher.model.Product;
import cs4330.cs.utep.mypricewatcher.model.WifiActivity;

/**
 * Main activity used to display the list of products, watching prices and handles actions from opening
 * custom views to find products, adding, editing, remove, and browsing each product URL.
 *
 * @author Isaias Leos
 */
public class MainActivity extends AppCompatActivity implements NewProductDialogActivity.NewProductDialogListener,
        EditProductDialogActivity.EditProductDialogListener, ListAdapter.Listener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Product> listOfItems = new ArrayList<>();
    private Product productHandler = new Product();
    private ListView productView;
    private PriceFinder priceFinder = new PriceFinder();
    private DatabaseHandler dataBase;
    private ProgressBar progressBar;

    /**
     * Initializes the layout, creates the toolbar, and handles sharing of a link from another
     * application.
     *
     * @param savedInstanceState instance saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        productView = findViewById(R.id.listView);
        dataBase = new DatabaseHandler(this);
        listOfItems = dataBase.getAll();
        runOnUiThread(this::renewList);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            for (int i = 0; i < listOfItems.size(); i++) {
                getPrice(i, false);
            }
            renewList();
            swipeRefreshLayout.setRefreshing(false);
        });
        handleShare(getIntent());
    }

    /**
     * This method will be invoke when the activity that was hidden comes back to view on the
     * screen to check if wifi is enabled and give a dialog box to force the user to enable it.
     */
    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnected()) {
        } else if (mobile.isConnected()) {
            WifiActivity wifiActivity = new WifiActivity();
            wifiActivity.setCancelable(false);
            wifiActivity.show(getSupportFragmentManager(), "Wifi Menu");
        }
    }

    /**
     * This method initializes the contents of the toolbar.
     *
     * @param menu menu of actions
     * @return if activity was created correctly
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method is called whenever an icon is clicked from the toolbar.
     * Handles each button's action.
     *
     * @param item the menu item selected
     * @return if an item was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                for (int i = 0; i < listOfItems.size(); i++) {
                    getPrice(i, false);
                }
                return true;
            case R.id.action_add:
                openNewProductDialog(null);
                return true;
            case R.id.openAmazon:
                toBrowser("https://www.amazon.com");
                return true;
            case R.id.openEbay:
                toBrowser("https://www.ebay.com");
                return true;
            case R.id.openWalmart:
                toBrowser("https://www.walmart.com");
                return true;
            case R.id.openHomedepot:
                toBrowser("https://www.homedepot.com");
                return true;
            case R.id.openAbout:
                openAboutDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Refresh data within the adapter.
     */
    private void renewList() {
        ListAdapter listAdapter = new ListAdapter(this, listOfItems);
        productView.setAdapter(listAdapter);
        productView.deferNotifyDataSetChanged();
    }

    /**
     * This method creates a dialog window to add a new product.
     *
     * @param sharedText url being shared from other applications
     */
    private void openNewProductDialog(String sharedText) {
        NewProductDialogActivity dialog = new NewProductDialogActivity();
        if (sharedText != null) {
            Bundle bundle = new Bundle();
            bundle.putString("url", sharedText);
            dialog.setArguments(bundle);
        }
        dialog.show(getSupportFragmentManager(), "New item added");
    }

    /**
     * Create a user dialog box to show who created the application.
     */
    private void openAboutDialog() {
        Log.e("About", "Dialog Opened");
        AboutActivity dialog = new AboutActivity();
        dialog.show(getSupportFragmentManager(), "About Menu");
    }

    /**
     * This method creates a dialog window to edit a currently existing item from the list.
     *
     * @param index location within the list
     */
    private void editProductDialog(int index) {
        EditProductDialogActivity dialog = new EditProductDialogActivity();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        bundle.putString("currentName", listOfItems.get(index).getName());
        bundle.putString("currentUrl", listOfItems.get(index).getURL());
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "Edit item");
    }

    /**
     * This method adds a new product and inserts into the fragment list.
     *
     * @param name name of product
     * @param url  url of product
     */
    @Override
    public void addProduct(String name, String url) {
        Product toAdd = new Product(url, name, 0.00, 0.00, 0.00);
        listOfItems.add(toAdd);
        getPrice(listOfItems.indexOf(toAdd), true);
    }

    /**
     * This method deletes a product from the list.
     *
     * @param index location within the list
     */
    public void deleteProduct(int index) {
        dataBase.delete(listOfItems.get(index).getId());
        listOfItems.remove(index);
        renewList();
    }

    /**
     * This method updates current products given its index.
     *
     * @param name  new name of product
     * @param url   new url of the product
     * @param index location within the list
     */
    @Override
    public void updateProduct(String name, String url, int index) {
        listOfItems.get(index).setName(name);
        listOfItems.get(index).setURL(url);
        dataBase.update(listOfItems.get(index));
        renewList();
    }


    /**
     * This method edits a product.
     *
     * @param index position of the product
     */
    public void editProduct(int index) {
        editProductDialog(index);
    }

    /**
     * Refresh the price of a product given the index.
     *
     * @param index position of the product
     */
    @Override
    public void refreshProduct(int index) {
        getPrice(index, false);
    }

    /**
     * Obtain the price of an item using the URL. Will check if the product is newly added or updating.
     *
     * @param index position of index
     * @param isNew if item is new
     */
    public void getPrice(int index, boolean isNew) {
        Thread priceThread = new Thread(() -> {
            productHandler = priceFinder.getPrice(listOfItems.get(index), this);
            Log.e("Handle Price", String.valueOf(productHandler.getCurrentPrice()));
        });
        priceThread.start();

        progressBar.setVisibility(ProgressBar.VISIBLE);
        Thread uiThread = new Thread(() -> {
            while (true) {
                if (!priceThread.isAlive()) {
                    break;
                }
            }
            runOnUiThread(() -> {
                if (isNew) {
                    dataBase.add(productHandler);
                } else {
                    dataBase.update(productHandler);
                }
                renewList();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            });
        });
        uiThread.start();
    }

    /**
     * Open a chrome custom tab given the product.
     *
     * @param url product's url
     */
    public void toBrowser(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.addDefaultShareMenuItem();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    /**
     * Display the web page of the given index from the list.
     *
     * @param index position of the product
     */
    public void openProductURL(int index, boolean isInternal) {
        if (isInternal) {
            toBrowser(listOfItems.get(index).getURL());
        } else {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(listOfItems.get(index).getURL()));
            startActivity(i);
        }
    }

    /**
     * Handles shared text and opens a new dialog to add the product to the current list.
     *
     * @param intent position of the product
     */
    private void handleShare(Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    openNewProductDialog(sharedText);
                }
            }
        }
    }
}