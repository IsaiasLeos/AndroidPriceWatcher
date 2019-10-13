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

public class MainActivity extends AppCompatActivity implements NewItemDialogActivity.NewItemDialogListener,
        EditItemDialogActivity.EditItemDialogListener, PatternListFragment.Listener {

    PatternListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragment = (PatternListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        handleShare(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                fragment.getAll().reloadManager();
                fragment.refreshList();
                return true;
            case R.id.action_add:
                openNewItemDialog(null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openNewItemDialog(String sharedText) {
        NewItemDialogActivity dialog = new NewItemDialogActivity();
        if (sharedText != null) {
            Bundle bundle = new Bundle();
            bundle.putString("url", sharedText);
            dialog.setArguments(bundle);
        }
        dialog.show(getSupportFragmentManager(), "New item added");
    }

    @Override
    public void addItem(String name, String url, String price, String date) {
        fragment.add(new Product(url, name, Double.parseDouble(price), Double.parseDouble(price), 0.00));
        fragment.refreshList();
    }

    @Override
    public void updateItem(String name, String url, int index) {
        fragment.get(index).setName(name);
        fragment.get(index).setURL(url);
        fragment.refreshList();
    }

    public void toBrowser(Product product) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(product.getURL()));
    }

    public void deleteItem(int index) {
        fragment.remove(index);
        fragment.refreshList();
    }

    public void editItem(int index) {
        EditItemDialogActivity dialog = new EditItemDialogActivity();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        bundle.putString("currentName", fragment.get(index).getName());
        bundle.putString("currentUrl", fragment.get(index).getURL());
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "Edit item");
    }

    public void displayWebsite(int index) {
        toBrowser(fragment.get(index));
    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            openNewItemDialog(sharedText);
        }
    }

    private void handleShare(Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            }
        }
    }
}
