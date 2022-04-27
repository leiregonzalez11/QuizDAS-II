package com.example.quizdas;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.quizdas.Inicio;
import com.example.quizdas.R;

public class CorrectDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Intent jugar = new Intent(getActivity(), Jugar.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_titulocorrecta);
        builder.setMessage(R.string.dialog_correcta);
        builder.setPositiveButton(R.string.dialog_buttoncorrectaaceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
                //startActivity(jugar);
            }
        });

        return builder.create();
    }
}