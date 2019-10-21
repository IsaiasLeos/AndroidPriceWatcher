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

/**
 * This class is used to display a dialog window that allows the user to edit a product.
 *
 * @author Isaias Leos
 */
public class NewProductDialogActivity extends AppCompatDialogFragment {

    //Text fields displayed in the dialog
    private EditText productName;
    private EditText productURL;
    //Listener used to call methods inside an activity
    private NewProductDialogListener listener;

    /**
     * Creates a custom Dialog Window.
     *
     * @param saveInstanceState last instance saved
     * @return instance to be displayed by the activity
     */
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_new_product_dialog, null);
        productName = view.findViewById(R.id.editNameString);
        productURL = view.findViewById(R.id.editURLString);
        builder.setView(view).setTitle("Adding Product...")
                //Action when user presses cancel button.
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                //Action when user presses cancel button.
                .setPositiveButton("OK", (dialog, which) -> {
                    String name = productName.getText().toString();
                    String url = productURL.getText().toString();
                    if (!name.equals("") && !url.equals("")) {
                        listener.addProduct(name, url);
                    } else {
                        //Inform user that an activity was empty
                        Toast.makeText(getContext(), getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
                    }
                });
        //If a user shared a link from another app
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
    public void onAttach(Context context) {
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
