package com.example.taskmaster.Adapter;

//import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.Api21Impl.getResources;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmaster.AddNewTask;
import com.example.taskmaster.MainActivity;
import com.example.taskmaster.Model.ToDoModel;
import com.example.taskmaster.Utils.DatabaseHandler;
import com.example.test.R;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> todoList;
    private final MainActivity activity;
    private final DatabaseHandler db;
    public ToDoAdapter(DatabaseHandler db,MainActivity activity){
        this.activity=activity;
        this.db=db;
       // this.todoList = new ArrayList<>();
    }
    public int getItemCount(){
        return todoList.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
}
    public void setTasks(List<ToDoModel> todoList){
        this.todoList = todoList;
        notifyItemInserted(todoList.size() - 1);
        notifyDataSetChanged();
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasklayout, parent, false);
        return new ViewHolder(itemView);
    }
    public Context getContext(){
        return activity;
    }
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (todoList == null) {
            return;
        }
        db.openDatabase();
        ToDoModel item=todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(),1);
                }
                else {
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }
     public void editItem(int position){
        ToDoModel item=todoList.get(position);
         Bundle bundle = new Bundle();
         bundle.putInt("id",item.getId());
         bundle.putString("task",item.getTask());
         AddNewTask fragment = new AddNewTask();
         fragment.setArguments(bundle);
         fragment.show(activity.getSupportFragmentManager(),AddNewTask.TAG);
         notifyItemChanged(position);
     }
    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
            CheckBox task;
            ViewHolder(View view){
                super(view);
                task=view.findViewById(R.id.todo);
            }
    }

    }

