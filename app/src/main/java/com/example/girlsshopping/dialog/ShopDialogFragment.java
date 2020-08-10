package com.example.girlsshopping.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.girlsshopping.R;

public class ShopDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.star_foreground);
        builder.setTitle("Gratulacje!");
        builder.setMessage("Właśnie kupiłeś przedmiot \npoprzez Kup Teraz :)");
        builder.setCancelable(false);

        builder.setNegativeButton("Super", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog=builder.create();
        return alertDialog;


    }
}