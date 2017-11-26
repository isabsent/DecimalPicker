package com.example.decimalpickerdemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.example.decimalpicker.DecimalPicker;

public class DecimalPickerDialog extends AppCompatDialogFragment {
    DecimalPicker decimalPicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_decimal_picker, null);

        decimalPicker = (DecimalPicker) view.findViewById(R.id.decimal_picker);
        decimalPicker.setFormat("%.3f");
        decimalPicker.setRange(0.0, 10.0);

        decimalPicker.setOnClickListener(new DecimalPicker.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

        decimalPicker.setOnValueChangeListener(new DecimalPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(DecimalPicker picker, double oldValue, double newValue) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("                  ")
                .setView(view)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity) getActivity()).setPickedNumber(decimalPicker.getNumber()); //Sorry for that! :)
                    }
                })
                .setNegativeButton(android.R.string.no, null);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() { //http://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked

            @Override
            public void onShow(final DialogInterface dialog) {
                decimalPicker.setNumber("1");
            }
        });

        return dialog;
    }

    private double getPickedValue(){
        return Double.parseDouble(decimalPicker.getNumber());
    }
}
