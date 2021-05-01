package com.bit.viandermobile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static org.apache.commons.lang3.StringUtils.join;

public class AboutDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_DeviceDefault_Light));
        View customView = this.getLayoutInflater().inflate(R.layout.fragment_about, null);
        builder.setView(customView);
        builder.setTitle(R.string.titulo_acerca_de)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        String versionName = BuildConfig.VERSION_NAME;
        TextView versionTextView = customView.findViewById(R.id.version);
        versionTextView.setText(join(getString(R.string.version), " ", versionName));
        return builder.create();
    }

}