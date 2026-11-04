package com.example.onlinedatabase;

import static com.example.onlinedatabase.UserStaticInfo.POSITION;
import static com.example.onlinedatabase.UserStaticInfo.users;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    int positionActiveUser;
    ListView listView;
    Context context;
    LayoutInflater layoutInflater;
    static UserListAdapter userListAdapter;

    //Панель профиля пользователя
    FrameLayout UserPanel;
    static TextView NameTextView, StateTextView, AgeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new UserStaticInfo();
        Init();
        GoToUserProfile(1);
    }

    public void GoToUserProfile(int position) {
        Intent intent = new Intent(context,UserActivity.class);
        intent.putExtra(POSITION, position);
        startActivity(intent);
    }
    public static void UpdateListAndUserPanel(User user) {
        userListAdapter.notifyDataSetChanged();
        InitPanel(user);
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

    public void EditUser(View view) {
        GoToUserProfile(positionActiveUser);
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

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View currentView, ViewGroup parent) {
            User currentUser = getItem(position);
            currentView = layoutInflater.inflate(R.layout.item_user, parent, false);

            TextView nameView = currentView.findViewById(R.id.NameTextView);
            TextView stateView = currentView.findViewById(R.id.StateTextView);

            //получем StateRound из currentView(который содержит R.layout.item_user)
            FrameLayout StateRound = currentView.findViewById(R.id.StateRound);
            //установка отображения статуса сигнала
            switch (currentUser.getStateSignal())
            {
                case 0:
                    StateRound.setBackgroundResource(R.drawable.back_offline);
                    break;
                case 1:
                    StateRound.setBackgroundResource(R.drawable.back_online);
                    break;
                case 2:
                    StateRound.setBackgroundResource(R.drawable.back_departed);
                    break;
            }

            nameView.setText(currentUser.getName());
            stateView.setText(currentUser.getState());

            currentView.setOnClickListener((view) -> {
                positionActiveUser = position;
                InitPanel(getItem(position));
                UserVisibility(true);
            });
            return currentView;
        }
    }

    private static void InitPanel(User item) {
        NameTextView.setText(item.getName());
        StateTextView.setText(item.getState());
        AgeTextView.setText(String.valueOf(item.getAge()));
    }
}