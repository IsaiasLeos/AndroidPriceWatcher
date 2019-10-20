package cs4330.cs.utep.pricewatcher.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;

import cs4330.cs.utep.pricewatcher.R;
import cs4330.cs.utep.pricewatcher.model.PatternListFragment;
import cs4330.cs.utep.pricewatcher.model.Product;

/**
 * Main activity used to display the list of products, watching prices and handles actions from opening
 * custom views to find products, adding, editing, remove, and browsing each product URL.
 *
 * @author Isaias Leos
 */
public class MainActivity extends AppCompatActivity implements NewProductDialogActivity.NewProductDialogListener,
        EditProductDialogActivity.EditProductDialogListener, PatternListFragment.Listener {

    //List fragment, contains list, modifies, adds, and deletes
    PatternListFragment fragment;

    /**
     * Initializes the layout, creates the toolbar, and handles sharing of a link from another
     * application.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initialize the list layout
        fragment = (PatternListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        //Handle link sharing from other applications
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
                fragment.getAll().refreshProductList();
                fragment.refreshList();
                return true;
            case R.id.action_add:
                openNewProductDialog(null);
                return true;
            case R.id.action_open_find:
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                builder.addDefaultShareMenuItem();
                customTabsIntent.launchUrl(this, Uri.parse("https://www.amazon.com"));
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
        bundle.putString("currentName", fragment.get(index).getName());
        bundle.putString("currentUrl", fragment.get(index).getURL());
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
        fragment.add(new Product(url, name, Double.parseDouble(price), Double.parseDouble(price), 0.00));
        fragment.refreshList();
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
        fragment.get(index).setName(name);
        fragment.get(index).setURL(url);
        fragment.refreshList();
    }


    /**
     * This method deletes a product from the list.
     *
     * @param index location within the list
     */
    public void deleteProduct(int index) {
        fragment.remove(index);
        fragment.refreshList();
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
     * @param product product
     */
    public void toBrowser(Product product) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(product.getURL()));
    }

    /**
     * Display the web page of the given index from the list.
     *
     * @param index
     */
    public void openProductURL(int index) {
        toBrowser(fragment.get(index));
    }

    /**
     * Refresh the price of a product given the index.
     *
     * @param index
     */
    @Override
    public void refreshProduct(int index) {
        fragment.getAll().refreshProduct(index);
        fragment.refreshList();
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
