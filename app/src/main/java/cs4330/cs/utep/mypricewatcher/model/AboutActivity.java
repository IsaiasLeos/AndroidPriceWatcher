package cs4330.cs.utep.mypricewatcher.model;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.browser.customtabs.CustomTabsIntent;

import java.util.Objects;

import cs4330.cs.utep.mypricewatcher.R;

public class AboutActivity extends AppCompatDialogFragment {

    /**
     * Creates a custom Dialog Window to show information about the app creator.
     *
     * @param saveInstanceState last instance saved
     * @return instance to be displayed by the activity
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_about, null);
        TextView about = view.findViewById(R.id.about);
        about.setText(Html.fromHtml("<html>"
                + "<center>"
                + "<strong>PriceWatcher v3</strong>"
                + "<br>Creators:<br><br><i><sup>Isaias Leos</sup>"
                + "</i><br><strong><a href=\"https://github.com/IsaiasLeos/AndroidPriceWatcher\">Github</a><strong></center></html>"));
        about.setOnClickListener(v -> {
            CustomTabsIntent.Builder customChromeTab = new CustomTabsIntent.Builder();
            customChromeTab.addDefaultShareMenuItem();
            CustomTabsIntent customTabsIntent = customChromeTab.build();
            customTabsIntent.launchUrl(Objects.requireNonNull(getContext()), Uri.parse("https://github.com/IsaiasLeos/AndroidPriceWatcher"));
        });
        builder.setView(view).setPositiveButton("Close", (dialog, which) -> {
        });
        return builder.create();
    }
}
