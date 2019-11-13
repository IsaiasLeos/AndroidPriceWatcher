package cs4330.cs.utep.mypricewatcher.model;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

import cs4330.cs.utep.mypricewatcher.R;

public class WifiActivity extends AppCompatDialogFragment {

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
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_wifi, null);
        TextView wifiTextView = view.findViewById(R.id.wifiTitle);
        TextView wifiDescription = view.findViewById(R.id.wifiDescription);
        wifiTextView.setText(Html.fromHtml("<html>"
                + "<center>"
                + "<strong>Warning</strong></center></html>"));
        wifiDescription.setText(Html.fromHtml("<html>"
                + "<center>You are currently not on Wifi!<br><br>"
                + "Will you like to be redirected?<br></center></html>"));
        wifiTextView.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)));
        builder.setView(view).setPositiveButton("Ok", (dialog, which) -> {
            Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
            startActivity(intent);
        });
        return builder.create();
    }


}
