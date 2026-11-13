package com.example.onlinedatabase.chats;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinedatabase.R;
import com.example.onlinedatabase.User;
import com.example.onlinedatabase.UserStaticInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private ListView listView;
    private List<User> chatUsers = new ArrayList<>();
    private ChatUserAdapter adapter;
    private String currentUserId;
    private java.util.Map<String, Integer> unreadMap = new java.util.HashMap<>();

    private void loadUnreadCounts() {
        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("Chats");

        chatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                unreadMap.clear();

                for (DataSnapshot chatSnap : snapshot.getChildren()) {
                    String chatId = chatSnap.getKey();
                    if (chatId == null) continue;

                    if (!chatId.contains(currentUserId)) continue;

                    Long lastRead = chatSnap.child("participants").child(currentUserId)
                            .child("lastReadTimestamp").getValue(Long.class);
                    if (lastRead == null) lastRead = 0L;

                    int unreadCount = 0;
                    for (DataSnapshot msgSnap : chatSnap.child("messages").getChildren()) {
                        Message msg = msgSnap.getValue(Message.class);
                        if (msg != null && msg.getTimestamp() > lastRead
                                && !msg.getSenderId().equals(currentUserId)) {
                            unreadCount++;
                        }
                    }

                    if (unreadCount > 0) {
                        String[] parts = chatId.split("_");
                        String otherId = parts[0].equals(currentUserId) ? parts[1] : parts[0];
                        unreadMap.put(otherId, unreadCount);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        currentUserId = UserStaticInfo.profileId;
        listView = findViewById(R.id.chatsListView);
        adapter = new ChatUserAdapter();
        listView.setAdapter(adapter);

        loadUsersFromFirebase();
        loadUnreadCounts();
    }

    private void loadUsersFromFirebase() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance()
                .getReference(UserStaticInfo.USERS_PROFILE_INFO);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatUsers.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String id = child.getKey();

                    if (id != null && id.equals(currentUserId)) continue;

                    String name = child.child(UserStaticInfo.NAME).getValue(String.class);
                    String state = child.child(UserStaticInfo.STATE).getValue(String.class);
                    Integer age = child.child(UserStaticInfo.AGE).getValue(Integer.class);

                    if (name != null) {
                        User u = new User(name, state != null ? state : "", age != null ? age : 0, 1);
                        u.setId(id);
                        chatUsers.add(u);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ContactsActivity.this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class ChatUserAdapter extends BaseAdapter {
        @Override
        public int getCount() { return chatUsers.size(); }

        @Override
        public User getItem(int position) { return chatUsers.get(position); }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(ContactsActivity.this)
                        .inflate(R.layout.item_contact, parent, false);
            }

            User user = getItem(position);

            TextView nameText = convertView.findViewById(R.id.nameText);
            TextView stateText = convertView.findViewById(R.id.stateText);
            TextView badge = convertView.findViewById(R.id.unreadBadge);

            nameText.setText(user.getName());
            stateText.setText(user.getState());

            Integer unreadCount = unreadMap.get(user.getId());
            if (unreadCount != null && unreadCount > 0) {
                badge.setVisibility(View.VISIBLE);
                badge.setText(String.valueOf(unreadCount));
            } else {
                badge.setVisibility(View.GONE);
            }

            convertView.setOnClickListener(v -> {
                String chatId = getChatId(currentUserId, user.getId());
                Intent intent = new Intent(ContactsActivity.this, ChatActivity.class);
                intent.putExtra("CHAT_ID", chatId);
                intent.putExtra("OTHER_USER_ID", user.getId());
                startActivity(intent);
            });

            return convertView;
        }
    }

    private String getChatId(String id1, String id2) {
        if (id1 == null || id2 == null) {
            return "Ошибка";
        }
        if (id1.compareTo(id2) < 0) return id1 + "_" + id2;
        else return id2 + "_" + id1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsersFromFirebase();
        loadUnreadCounts();
    }
}