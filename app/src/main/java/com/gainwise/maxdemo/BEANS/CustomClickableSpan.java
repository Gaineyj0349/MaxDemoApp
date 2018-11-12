package com.gainwise.maxdemo.BEANS;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.style.ClickableSpan;
import android.view.View;
import com.gainwise.maxdemo.Activity.ShowActivity;
import spencerstudios.com.bungeelib.Bungee;

public class CustomClickableSpan extends ClickableSpan {
    String name;
    Context context;

    public CustomClickableSpan(String name, Context context) {
        this.name = name;
        this.context = context;
    }

    @Override
    public void onClick(@NonNull View view) {

       Intent intent = new Intent(context, ShowActivity.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       intent.putExtra("name", name);
       context.startActivity(intent);
       Bungee.diagonal(context);
    }


}
