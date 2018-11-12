package com.gainwise.maxdemo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gainwise.maxdemo.Activity.ShowActivity;
import com.gainwise.maxdemo.R;
import spencerstudios.com.bungeelib.Bungee;

import java.util.List;

public class RVNameAdapter extends RecyclerView.Adapter<RVNameAdapter.Holder> {

    private Context myContext;
    private List<String> nameList;


    public RVNameAdapter(Context myContext, List<String> nameList) {
        this.myContext = myContext;
        this.nameList = nameList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(myContext);
        View view = inflater.inflate(R.layout.rv_item_name, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

    holder.textView.setText(nameList.get(i));

    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        public TextView textView;

        public Holder(@NonNull final View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.rv_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = textView.getText().toString();
                    Intent intent = new Intent(myContext, ShowActivity.class);
                    intent.putExtra("name", name);
                    myContext.startActivity(intent);
                    Bungee.card(myContext);
                }
            });
        }
    }
}
