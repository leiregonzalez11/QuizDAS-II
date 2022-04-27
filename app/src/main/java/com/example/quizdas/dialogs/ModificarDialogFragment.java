package com.example.quizdas.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.quizdas.R;
import com.example.quizdas.activities.Inicio;

public class ModificarDialogFragment extends DialogFragment {

    String email;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);


        email = getArguments().getString("email");

        Intent bienvenida = new Intent(getActivity(), Inicio.class);
        bienvenida.putExtra("email", email);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_modificar_titulo);
        builder.setMessage(R.string.dialog_modificar);
        builder.setPositiveButton(R.string.dialog_modif_buttonaceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
                startActivity(bienvenida);
            }
        });

        return builder.create();
    }
}