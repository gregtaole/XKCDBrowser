package fr.esiea.xkcdbrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class NetworkConnectivityDialogFragment extends DialogFragment {
    NetworkConnectivityListener networkConnectivityListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("XKCDBrowser requires network connectivity. Go to network settings ?")
                .setPositiveButton("Wifi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        networkConnectivityListener.onDialogPositiveClick(NetworkConnectivityDialogFragment.this);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        networkConnectivityListener.onDialogNeutralClick(NetworkConnectivityDialogFragment.this);
                    }
                })
                .setNegativeButton("Data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        networkConnectivityListener.onDialogNegativeClick(NetworkConnectivityDialogFragment.this);
                    }
                })
                .setCancelable(false);
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            networkConnectivityListener = (NetworkConnectivityListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NetworkConnectivityListener");
        }
    }

    public interface NetworkConnectivityListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNeutralClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
}
