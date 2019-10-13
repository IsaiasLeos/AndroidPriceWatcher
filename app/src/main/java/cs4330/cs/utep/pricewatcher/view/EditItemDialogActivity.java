package cs4330.cs.utep.pricewatcher.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

import cs4330.cs.utep.pricewatcher.R;

public class EditItemDialogActivity extends AppCompatDialogFragment {

    private EditText itemsName;
    private EditText itemsUrl;
    private EditItemDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_edit_item_dialog, null);
        builder.setView(view).setTitle("Edit Item")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .setPositiveButton("Ok", (dialog, which) -> {
                    String name = itemsName.getText().toString();
                    String url = itemsUrl.getText().toString();
                    if (!name.equals("") && !url.equals("")) {
                        assert getArguments() != null;
                        listener.updateItem(name, url, getArguments().getInt("index"));
                    } else {
                        Toast.makeText(getContext(), getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
                    }
                });
        itemsName = view.findViewById(R.id.editNameString1);
        itemsUrl = view.findViewById(R.id.editURLString1);
        assert getArguments() != null;
        itemsName.setText(getArguments().getString("currentName"));
        itemsUrl.setText(getArguments().getString("currentUrl"));
        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (EditItemDialogListener) context;
    }

    public interface EditItemDialogListener {
        void updateItem(String name, String url, int index);
    }
}
