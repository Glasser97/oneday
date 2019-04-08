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

public class addDialogFragment extends DialogFragment implements View.OnClickListener {

    int option;
    public OnDialogChosenListener mlistener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.add_dialog, container, false);

        TextView takephoto = (TextView) view.findViewById(R.id.take_a_photo);
        TextView openalbum = (TextView) view.findViewById(R.id.open_album);

        takephoto.setOnClickListener(this);
        openalbum.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.take_a_photo:
                option = 1;
                Toast.makeText(getActivity(), "take a photo.",
                        Toast.LENGTH_LONG).show();
                break;
            case R.id.open_album:
                option = 2;
                Toast.makeText(getActivity(), "choose a image in your album",
                        Toast.LENGTH_LONG).show();
                break;
        }
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

    public interface OnDialogChosenListener {
        void onDialogChosen(int option);
    }

    public void setOnDialogListener(OnDialogChosenListener dialogListener){
        this.mlistener = dialogListener;
    }



}
