package com.example.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdaptor extends RecyclerView.Adapter<ListAdaptor.ViewHolder>{

    List<String> toDoList;
    OnLongClickListener longClick;
    OnClickListener click;

    public interface OnClickListener{
        void OnItemClicked(int position);
    }

    public interface OnLongClickListener{
        void OnItemLongClicked(int position);
    }

    public ListAdaptor(List<String> list, OnLongClickListener longClick, OnClickListener click){
        this.toDoList=list;
        this.longClick=longClick;
        this.click=click;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vh=LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(vh);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      String item=toDoList.get(position);
        holder.append(item);
    }

    //how many items in the view holder
    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv=itemView.findViewById(android.R.id.text1);
        }

        public void append(String s) {
            tv.setText(s);
            tv.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view){
                  longClick.OnItemLongClicked(getAdapterPosition());
                  return true;
                }
            });
            tv.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    click.OnItemClicked(getAdapterPosition());
                }
            });
        }
    }
}