package com.example.onlinedatabase;

import static com.example.onlinedatabase.UserStaticInfo.POSITION;
import static com.example.onlinedatabase.UserStaticInfo.users;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {
    private User activeUser;
    private EditText NameTextView, StateTextView, AgeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        int position = getIntent().getIntExtra(POSITION, 0);
        activeUser = users.get(position);
        Init();
        setUserInfo();
    }

    private void setUserInfo() {
        NameTextView.setText(activeUser.getName());
        StateTextView.setText(activeUser.getState());
        AgeTextView.setText(String.valueOf(activeUser.getAge()));
    }
    private void Init() {
        NameTextView = findViewById(R.id.NameTextView);
        StateTextView = findViewById(R.id.StateTextView);
        AgeTextView = findViewById(R.id.AgeTextView);
    }
    public void Back(View view) {
        onBackPressed();
    }
    public void Save(View view) {
        activeUser.setName(NameTextView.getText().toString());
        activeUser.setState(StateTextView.getText().toString());
        String age = AgeTextView.getText().toString();
        try {
            activeUser.setAge(Integer.parseInt(age));
        }
        catch (Exception NumberFormatException) {
            //вносим возраст предыдущего пользователя
            activeUser.setAge(activeUser.getAge());
        }

        MainActivity.UpdateListAndUserPanel(activeUser);
        finish();
    }
}