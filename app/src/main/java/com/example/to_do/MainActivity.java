package com.example.to_do;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private EditText taskEditText;
    private Button addButton;
    private ListView taskListView;
    private TextView taskCountTextView;
    private ArrayList<String> tasks;
    private ArrayAdapter<String> taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskEditText = findViewById(R.id.taskEditText);
        addButton = findViewById(R.id.addButton);
        taskListView = findViewById(R.id.taskListView);
        taskCountTextView = findViewById(R.id.taskCountTextView);

        tasks = new ArrayList<>();
        taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasks);
        taskListView.setAdapter(taskAdapter);

        updateTaskCount();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = taskEditText.getText().toString();
                if (!task.isEmpty()) {
                    tasks.add(task);
                    taskAdapter.notifyDataSetChanged();
                    updateTaskCount();
                    taskEditText.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                showEditDialog(position);
            }
        });

        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });
    }

    private void showEditDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");
        final EditText editText = new EditText(this);
        editText.setText(tasks.get(position));
        builder.setView(editText);
        builder.setPositiveButton("Save", (dialogInterface, i) -> {
            String newTask = editText.getText().toString();
            tasks.set(position, newTask);
            taskAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure you want to delete this task?");
        builder.setPositiveButton("Delete", (dialogInterface, i) -> {
            tasks.remove(position);
            taskAdapter.notifyDataSetChanged();
            updateTaskCount();
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void updateTaskCount() {
        int count = tasks.size();
        String countText = "Total Tasks: " + count;
        taskCountTextView.setText(countText);
    }
}