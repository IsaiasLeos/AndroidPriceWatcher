package cs4330.cs.utep.pricewatcher.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.ListFragment;

import java.util.List;
import java.util.Objects;

import cs4330.cs.utep.pricewatcher.R;
import cs4330.cs.utep.pricewatcher.controller.ProductManager;

public class PatternListFragment extends ListFragment {

    //Default Product
    private Product product1 = new Product("https://www.amazon.com/LG-V35-ThinQ-Alexa-Hands-Free/dp/B07D46BMYT", "LG V35", 399.99, 399.99, 0.00);
    private ProductManager productManager = new ProductManager();
    private ProductListAdapter adapter;
    private Listener listener;

    /**
     * Adds a product to the current list of products.
     * Refreshes the adapter afterwards.
     *
     * @param product
     */
    public void add(Product product) {
        productManager.add(product);
        refreshList();
    }

    /**
     * Removes a product from the current list of products given the index.
     * Refresh the adapter afterwards
     *
     * @param i number of the product to be removed
     */
    public void remove(int i) {
        productManager.delete(i);
        refreshList();
    }

    /**
     * Notifies the attached observers that the underlying data has been changed.
     */
    public void refreshList() {
        adapter.notifyDataSetChanged();
    }

    /**
     * Obtains a product given its index.
     *
     * @param i product number
     * @return product within the list
     */
    public Product get(int i) {
        return productManager.get().get(i);
    }

    /**
     * Obtains the list of products.
     *
     * @return list of products
     */
    public ProductManager getAll() {
        return productManager;
    }

    /**
     * Creates a custom list.
     *
     * @param inflater
     * @param container
     * @param savedInstanceBundle
     * @return
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {
        productManager.add(product1);
        adapter = new ProductListAdapter(this.getContext(), productManager.get());
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceBundle);
    }

    /**
     * This creates a popup menu when an product is clicked and performs and
     * action depending on what item was clicked.
     *
     * @param view
     * @param position The position of the clicked product in the list.
     */
    @SuppressLint("RestrictedApi")
    private boolean createPopup(View view, int position) {
        PopupMenu menu = new PopupMenu(Objects.requireNonNull(getContext()), view);
        menu.inflate(R.menu.popup_menu);
        menu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.popDelete:
                    listener.deleteProduct(position);
                    return true;
                case R.id.popBrowse:
                    listener.openProductURL(position);
                    return true;
                case R.id.popEdit:
                    listener.editProduct(position);
                    return true;
                case R.id.popRefreshProduct:
                    listener.refreshProduct(position);
                default:
                    return false;
            }
        });
        MenuPopupHelper menuHelper = new MenuPopupHelper(getContext(), (MenuBuilder) menu.getMenu(), Objects.requireNonNull(getView()));
        menuHelper.setForceShowIcon(true);
        menuHelper.setGravity(Gravity.END);
        menuHelper.show();
        return false;
    }

    /**
     * Launches custom chrome tab according to what item was clicked.
     *
     * @param url product's url
     */
    private void patternClicked(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(Objects.requireNonNull(getContext()), Uri.parse(url));
    }

    /**
     * Interfaces used to link the activity to the listener
     */
    public interface Listener {
        void deleteProduct(int index);

        void editProduct(int index);

        void openProductURL(int index);

        void refreshProduct(int index);
    }

    /**
     * Generate an products's actions and details.
     */
    private class ProductListAdapter extends ArrayAdapter<Product> {

        private final List<Product> productList;

        /**
         * Constructor called when creating the adapter.
         *
         * @param ctx     content
         * @param product list of products
         */
        ProductListAdapter(Context ctx, List<Product> product) {
            super(ctx, -1, product);
            this.productList = product;
            listener = (Listener) getContext();
        }

        /**
         * Generating an products's details and actions for a FragmentList.
         *
         * @param position    position of a product within the adapter
         * @param convertView old view to re-use
         * @param parent      parent view
         * @return
         */
        @SuppressLint("SetTextI18n")
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView != null ? convertView
                    : LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.simple_list_product, parent, false);
            //Obtain the product from the given position
            Product product = productList.get(position);
            ImageView image = row.findViewById(R.id.imageView);
            //Set the images according the url of the product
            if (product.getURL().contains("ebay")) {
                image.setImageResource(R.drawable.ebay);
            }
            if (product.getURL().contains("amazon")) {
                image.setImageResource(R.drawable.amazon);
            }
            if (product.getURL().contains("walmart")) {
                image.setImageResource(R.drawable.walmart);
            }
            //Change the text according to the product given
            TextView view = row.findViewById(R.id.productNameString);
            FrameLayout layout = row.findViewById(R.id.frameLayout);
            //Set actions from long pressing or quick pressing each layout
            layout.setOnLongClickListener((view1) -> createPopup(view1, position));
            layout.setOnClickListener((view1) -> patternClicked(product.getURL()));
            view.setText(Html.fromHtml("<b>" + product.getName() + "</b>"));
            view = row.findViewById(R.id.productChangeDouble);
            //Differentiate the color depending on positive or negative change
            String changeString = "0.00 %";
            if (product.getChange() > 0.0) {
                changeString = "-" + Math.abs(product.getChange()) + "%";
                view.setTextColor(Color.GREEN);
            } else if (product.getChange() < 0.0) {
                changeString = "+" + Math.abs(product.getChange()) + "%";
                view.setTextColor(Color.RED);
            }
            view.setText(changeString);
            view = row.findViewById(R.id.productCurrentPriceDouble);
            view.setText("$" + product.getCurrentPrice());
            view = row.findViewById(R.id.productInitialPriceDouble);
            view.setText("$" + product.getInitialPrice());
            view = row.findViewById(R.id.dateAddedString);
            view.setText(product.getDate());
            return row;
        }
    }
}
