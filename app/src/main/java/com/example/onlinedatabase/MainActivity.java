package com.example.onlinedatabase;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Context context;
    LayoutInflater layoutInflater;
    //Список пользователей объявим
    List<User> users = new ArrayList<>();
    UserListAdapter userListAdapter;

    //Панель профиля пользователя
    FrameLayout UserPanel;
    TextView NameTextView, StateTextView, AgeTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AddUsersInList();
        Init();
    }

    private void AddUsersInList() {
        users.add(new User("Алексей", "Я ушел", 18));
        users.add(new User("Андрей", "Я не буду работать", 17));
        users.add(new User("Максим", "Я анимешник", 19));
        users.add(new User("Дмитрий", "Отдохнуть хочу", 27));
        users.add(new User("Алексей", "Я ушел", 15));
        users.add(new User("Алексий", "Я умер", 91));
    }

    private void Init() {
        //Инициализация UserPanel
        UserPanel = findViewById(R.id.userPanel);
        NameTextView = findViewById(R.id.NameTextView);
        StateTextView = findViewById(R.id.StateTextView);
        AgeTextView = findViewById(R.id.AgeTextView);
        //Работа с listView
        listView = findViewById(R.id.listView);
        context = this;
        layoutInflater = LayoutInflater.from(context);
        userListAdapter = new UserListAdapter();
        listView.setAdapter(userListAdapter);
    }

    public void BackToList(View view) {
        UserVisibility(false);
    }

    private void UserVisibility(boolean visible) {
        if(visible)
            UserPanel.setVisibility(View.VISIBLE);
        else
            UserPanel.setVisibility(View.GONE);
    }

    private class UserListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public User getItem(int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View currentView, ViewGroup parent) {
            User currentUser = getItem(position);
            currentView = layoutInflater.inflate(R.layout.item_user, parent, false);

            TextView nameView = currentView.findViewById(R.id.NameTextView);
            TextView stateView = currentView.findViewById(R.id.StateTextView);

            nameView.setText(currentUser.getName());
            stateView.setText(currentUser.getState());

            currentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InitPanel(getItem(position));
                    UserVisibility(true);
                }
            });
            return currentView;
        }
    }

    private void InitPanel(User item) {
        NameTextView.setText(item.getName());
        StateTextView.setText(item.getState());
        AgeTextView.setText(String.valueOf(item.getAge()));
    }
}