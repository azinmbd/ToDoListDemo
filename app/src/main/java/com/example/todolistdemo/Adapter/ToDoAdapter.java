package com.example.todolistdemo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistdemo.AddNewTask;
import com.example.todolistdemo.MainActivity;
import com.example.todolistdemo.Model.ToDoModel;
import com.example.todolistdemo.R;
import com.example.todolistdemo.Utils.DatabaseHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> mList;
    private MainActivity activity;
    private DatabaseHelper myDB;

    public ToDoAdapter(DatabaseHelper myDB, MainActivity activity) {
        this.activity = activity;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.myCheckBox.setText(item.getTask());
        holder.myCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.myCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myDB.updateStatus(item.getId(), 1);
                } else {
                    myDB.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    public boolean toBoolean(int num) {
        return num != 0;
    }

    public Context getContext() {
        return activity;
    }

    public void setTask(List<ToDoModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position) {
        ToDoModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ToDoModel item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), task.getTag());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox myCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}
