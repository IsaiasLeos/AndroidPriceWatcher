package cs4330.cs.utep.mypricewatcher.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

import cs4330.cs.utep.mypricewatcher.R;

/**
 * This class is used to display a dialog window that allows the user to edit a product.
 *
 * @author Isaias Leos
 */
public class NewProductDialogActivity extends AppCompatDialogFragment {

    private EditText productName;
    private EditText productURL;
    private NewProductDialogListener listener;

    /**
     * Creates a custom Dialog Window.
     *
     * @param saveInstanceState last instance saved
     * @return instance to be displayed by the activity
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_new_product_dialog, null);
        productName = view.findViewById(R.id.editNameString);
        productURL = view.findViewById(R.id.editURLString);
        builder.setView(view).setTitle("Adding Product...")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .setPositiveButton("OK", (dialog, which) -> {
                    String name = productName.getText().toString();
                    String url = productURL.getText().toString();
                    if (!name.equals("") && !url.equals("")) {
                        listener.addProduct(name, url);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
                    }
                });
        if (getArguments() != null) {
            productURL.setText(getArguments().getString("url"));
        }
        return builder.create();
    }

    /**
     * Initialize listener within the implemented activity.
     *
     * @param context context of which method its attached to
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (NewProductDialogListener) context;
    }

    /**
     * Interface used to link the activity to the listener.
     */
    public interface NewProductDialogListener {
        void addProduct(String name, String url);
    }
}
