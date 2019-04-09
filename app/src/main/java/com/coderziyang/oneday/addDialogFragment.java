package com.coderziyang.oneday;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class addDialogFragment extends DialogFragment {

    int option;
    public OnDialogChosenListener mlistener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.add_dialog, container, false);

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        TextView takephoto = (TextView) getView().findViewById(R.id.take_a_photo);
        TextView openalbum = (TextView) getView().findViewById(R.id.open_album);

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option = 1;
                Toast.makeText(getActivity(), "take a photo", Toast.LENGTH_SHORT).show();
                mlistener.onDialogChosen(option);
                dismiss();
            }
        });

        openalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option = 2;
                Toast.makeText(getActivity(), "album", Toast.LENGTH_SHORT).show();
                mlistener.onDialogChosen(option);
                dismiss();
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public interface OnDialogChosenListener{
        void onDialogChosen(int option);
    }

    public void setOnDialogListener(OnDialogChosenListener dialogListener){
        this.mlistener = dialogListener;
    }

}
