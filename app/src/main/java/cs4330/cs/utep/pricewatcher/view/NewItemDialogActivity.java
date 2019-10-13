package cs4330.cs.utep.pricewatcher.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

import cs4330.cs.utep.pricewatcher.R;

public class NewItemDialogActivity extends AppCompatDialogFragment {

    private EditText productName;
    private EditText productURL;
    private EditText productPrice;
    private NewItemDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_new_item_dialog, null);
        productName = view.findViewById(R.id.editNameString);
        productURL = view.findViewById(R.id.editURLString);
        productPrice = view.findViewById(R.id.editPriceDouble);
        builder.setView(view).setTitle("Adding Item...")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .setPositiveButton("OK", (dialog, which) -> {
                    String name = productName.getText().toString();
                    String url = productURL.getText().toString();
                    String price = productPrice.getText().toString();
                    if (!name.equals("") && !name.equals("Name") && !url.equals("") && !price.equals("") && !price.equals("0.00")) {
                        listener.addItem(name, url, price, "");
                    } else {
                        Toast.makeText(getContext(), getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
                    }
                });
        if (getArguments() != null) {
            productURL.setText(getArguments().getString("url"));
        }
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (NewItemDialogListener) context;
    }

    public interface NewItemDialogListener {
        void addItem(String name, String url, String price, String date);
    }
}
