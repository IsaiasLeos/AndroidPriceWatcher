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

import androidx.appcompat.widget.PopupMenu;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.ListFragment;

import java.util.List;
import java.util.Objects;

import cs4330.cs.utep.pricewatcher.R;
import cs4330.cs.utep.pricewatcher.controller.ProductManager;

public class PatternListFragment extends ListFragment {

    private Product product1 = new Product("https://www.amazon.com/LG-V35-ThinQ-Alexa-Hands-Free/dp/B07D46BMYT", "LG V35", 399.99, 399.99, 0.00);
    private ProductManager productManager = new ProductManager();
    private ProductListAdapter adapter;
    private Listener listener;

    public void add(Product product) {
        productManager.add(product);
        refreshList();
    }

    public void remove(int i) {
        productManager.delete(i);
        refreshList();
    }

    public void refreshList() {
        adapter.notifyDataSetChanged();
    }

    public Product get(int i) {
        return productManager.get().get(i);
    }

    public ProductManager getAll() {
        return productManager;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {
        productManager.add(product1);
        adapter = new ProductListAdapter(this.getContext(), productManager.get());
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceBundle);
    }

    private boolean createPopup(View view, int position) {
        PopupMenu menu = new PopupMenu(Objects.requireNonNull(getContext()), view, Gravity.END);
        menu.inflate(R.menu.popup_menu);
        menu.show();
        menu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.popDelete:
                    listener.deleteItem(position);
                    return true;
                case R.id.popBrowse:
                    listener.displayWebsite(position);
                    return true;
                case R.id.popEdit:
                    listener.editItem(position);
                    return true;
                case R.id.popRefreshItem:
                    listener.refreshItem(position);
                default:
                    return false;
            }
        });
        return false;
    }

    private void patternClicked(Product product) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(Objects.requireNonNull(getContext()), Uri.parse(product.getURL()));
    }

    public interface Listener {
        void deleteItem(int index);

        void editItem(int index);

        void displayWebsite(int index);

        void refreshItem(int index);
    }

    private class ProductListAdapter extends ArrayAdapter<Product> {

        private final List<Product> productList;

        ProductListAdapter(Context ctx, List<Product> product) {
            super(ctx, -1, product);
            this.productList = product;
            listener = (Listener) getContext();
        }

        @SuppressLint("SetTextI18n")
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView != null ? convertView
                    : LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.simple_list_product, parent, false);
            Product item = productList.get(position);
            ImageView image = row.findViewById(R.id.imageView);
            if (item.getURL().contains("ebay")) {
                image.setImageResource(R.drawable.ebay);
            }
            if (item.getURL().contains("amazon")) {
                image.setImageResource(R.drawable.amazon);
            }
            if (item.getURL().contains("walmart")) {
                image.setImageResource(R.drawable.walmart);
            }
            TextView view = row.findViewById(R.id.productNameString);
            FrameLayout layout = row.findViewById(R.id.frameLayout);
            layout.setOnLongClickListener((view1) -> createPopup(view1, position));
            layout.setOnClickListener((view1) -> patternClicked(item));
            view.setText(Html.fromHtml("<b>" + item.getName() + "</b>"));
            view = row.findViewById(R.id.productChangeDouble);
            String changeString = "0.00 %";
            if (item.getChange() > 0.0) {
                changeString = "-" + Math.abs(item.getChange()) + "%";
                view.setTextColor(Color.GREEN);
            } else if (item.getChange() < 0.0) {
                changeString = "+" + Math.abs(item.getChange()) + "%";
                view.setTextColor(Color.RED);
            }
            view.setText(changeString);
            view = row.findViewById(R.id.productCurrentPriceDouble);
            view.setText("$" + item.getCurrentPrice());
            view = row.findViewById(R.id.productInitialPriceDouble);
            view.setText("$" + item.getInitialPrice());
            view = row.findViewById(R.id.dateAddedString);
            view.setText(item.getDate());
            return row;
        }
    }
}
