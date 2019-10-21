package cs4330.cs.utep.pricewatcher.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cs4330.cs.utep.pricewatcher.R;
import cs4330.cs.utep.pricewatcher.model.ListAdapter;
import cs4330.cs.utep.pricewatcher.model.Product;

/**
 * Main activity used to display the list of products, watching prices and handles actions from opening
 * custom views to find products, adding, editing, remove, and browsing each product URL.
 *
 * @author Isaias Leos
 */
public class MainActivity extends AppCompatActivity implements NewProductDialogActivity.NewProductDialogListener,
        EditProductDialogActivity.EditProductDialogListener, ListAdapter.Listener {

    private static List<Product> listOfItems = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private Product product1 = new Product("https://www.amazon.com/LG-V35-ThinQ-Alexa-Hands-Free/dp/B07D46BMYT", "LG V35", 399.99, 399.99, 0.00);
    private Product product2 = new Product("https://www.ebay.com/itm/Lenovo-Flex-5-Laptop-15-6-Touch-Screen-8th-Gen-Intel-Core-i7-8GB-Memory/193136537648", "Lenovo-Flex-5-Laptop", 999.99, 999.99, 0.00);
    private ListView productView;

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

        //Set the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        productView = findViewById(R.id.listView);
        if (listOfItems.size() <= 0) {
            listOfItems.add(product1);
            listOfItems.add(product2);
        }
        renewList();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            for (int i = 0; i < listOfItems.size(); i++) {
                listOfItems.get(i).refreshPrice();
            }
            renewList();
            swipeRefreshLayout.setRefreshing(false);
        });
        handleShare(getIntent());
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
        return true;
    }

    /**
     * Refresh data within the adapter.
     */
    private void renewList() {
        ListAdapter listAdapter = new ListAdapter(this, listOfItems);
        productView.setAdapter(listAdapter);
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
                    listOfItems.get(i).refreshPrice();
                }
                renewList();
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
        }
        return super.onOptionsItemSelected(item);
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
        bundle.putString("currentPrice", String.valueOf(listOfItems.get(index).getInitialPrice()));
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "Edit item");
    }

    /**
     * This method adds a new product and inserts into the fragment list.
     *
     * @param name  name of product
     * @param url   url of product
     * @param price initial price of product
     */
    @Override
    public void addProduct(String name, String url, String price) {
        listOfItems.add(new Product(url, name, Double.parseDouble(price), Double.parseDouble(price), 0.00));
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
    public void updateProduct(String name, String url, String price, int index) {
        listOfItems.get(index).setName(name);
        listOfItems.get(index).setURL(url);
        listOfItems.get(index).setInitialPrice(Double.parseDouble(price));
        renewList();
    }


    /**
     * This method deletes a product from the list.
     *
     * @param index location within the list
     */
    public void deleteProduct(int index) {
        listOfItems.remove(index);
        renewList();
    }

    /**
     * This method edits a product.
     *
     * @param index location within the list
     */
    public void editProduct(int index) {
        editProductDialog(index);
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
     * @param index
     */
    public void openProductURL(int index, boolean isInternal) {
        if (isInternal) {
            toBrowser(listOfItems.get(index).getURL());
        } else {
            shouldOverrideUrlLoading(listOfItems.get(index).getURL());
        }
    }

    public void shouldOverrideUrlLoading(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(i);
    }

    /**
     * Refresh the price of a product given the index.
     *
     * @param index
     */
    @Override
    public void refreshProduct(int index) {
        listOfItems.get(index).refreshPrice();
        renewList();
    }


    /**
     * Handles shared text and opens a new dialog to add the product to the current list.
     *
     * @param intent
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
