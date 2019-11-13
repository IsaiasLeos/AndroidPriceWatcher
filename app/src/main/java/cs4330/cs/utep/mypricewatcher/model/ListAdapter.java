package cs4330.cs.utep.mypricewatcher.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;
import java.util.Objects;

import cs4330.cs.utep.mypricewatcher.R;
import cs4330.cs.utep.mypricewatcher.view.ScrollingActivity;

public class ListAdapter extends ArrayAdapter<Product> {

    private final List<Product> productList;
    private Listener listener;

    /**
     * Constructor called when creating the adapter.
     *
     * @param ctx     content
     * @param product list of products
     */
    public ListAdapter(Context ctx, List<Product> product) {
        super(ctx, -1, product);
        this.productList = product;
        listener = (Listener) getContext();
    }

    /**
     * This creates a popup menu when an product is clicked and performs and
     * action depending on what item was clicked.
     *
     * @param view     current view
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
                case R.id.popBrowse1:
                    listener.openProductURL(position, true);
                    return true;
                case R.id.popBrowse2:
                    listener.openProductURL(position, false);
                    return true;
                case R.id.popEdit:
                    listener.editProduct(position);
                    return true;
                case R.id.popRefreshProduct:
                    listener.refreshProduct(position);
                    return true;
                default:
                    return false;
            }
        });
        MenuPopupHelper menuHelper = new MenuPopupHelper(getContext(), (MenuBuilder) menu.getMenu(), view);
        menuHelper.setForceShowIcon(true);
        menuHelper.setGravity(Gravity.END);
        menuHelper.show();
        return false;
    }

    /**
     * Generating an products's details and actions for a FragmentList.
     *
     * @param position    position of a product within the adapter
     * @param convertView old view to re-use
     * @param parent      parent view
     * @return current view
     */
    @NonNull
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView != null ? convertView
                : LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_list_product, parent, false);
        Product product = productList.get(position);
        ImageView icon = row.findViewById(R.id.imageView2);
        if (product.getURL().contains("amazon")) {
            icon.setImageResource(R.drawable.amazon256);
        }
        if (product.getURL().contains("ebay")) {
            icon.setImageResource(R.drawable.ebay256);
        }
        if (product.getURL().contains("walmart")) {
            icon.setImageResource(R.drawable.walmart256);
        }
        if (product.getURL().contains("homedepot")) {
            icon.setImageResource(R.drawable.homedepot256);
        }
        ConstraintLayout layout = row.findViewById(R.id.frameLayout);
        layout.setOnLongClickListener((view1) -> createPopup(view1, position));
        layout.setOnClickListener(v -> detailedClicked(position));
        TextView view = row.findViewById(R.id.productNameString);
        view.setText(Html.fromHtml(String.format("<b>%s</b>",
                product.getName())));
        TextView view2 = row.findViewById(R.id.description);
        String color = "";
        if (product.getChange() < 0) {
            color = "#008000";
        } else if (product.getChange() > 0) {
            color = "#FF0000";
        }
        view2.setText(Html.fromHtml(String.format("<b>Current Price:</b> $%.2f<br>" +
                        "<b>Change:</b> <font color='%s'>%.2f%%<br></font> " +
                        "<b>Date Added:</b> %s",
                product.getCurrentPrice(), color, product.getChange(), product.getDate())));
        return row;
    }

    /**
     * Go into the scrolling activity to show more detail information about the application.
     *
     * @param position
     */
    private void detailedClicked(int position) {
        Intent i = new Intent(getContext(), ScrollingActivity.class);
        i.putExtra("name", productList.get(position).getName());
        i.putExtra("init", productList.get(position).getInitialPrice());
        i.putExtra("curr", productList.get(position).getCurrentPrice());
        i.putExtra("change", productList.get(position).getChange());
        i.putExtra("date", productList.get(position).getDate());
        i.putExtra("url", productList.get(position).getURL());
        getContext().startActivity(i);
    }

    /**
     * Product listener interface for actions on the product.
     */
    public interface Listener {

        void deleteProduct(int index);

        void editProduct(int index);

        void openProductURL(int index, boolean isInternal);

        void refreshProduct(int index);
    }
}

