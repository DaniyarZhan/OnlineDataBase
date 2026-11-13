package com.example.onlinedatabase.chats;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinedatabase.R;
import com.example.onlinedatabase.UserStaticInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private String chatId, currentUserId, otherUserId;

    private ListView messagesListView;
    private EditText messageInput;
    private Button sendButton;

    private List<Message> messages = new ArrayList<>();
    private ArrayAdapter<Message> adapter;
    private DatabaseReference chatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatId = getIntent().getStringExtra("CHAT_ID");
        otherUserId = getIntent().getStringExtra("OTHER_USER_ID");
        currentUserId = UserStaticInfo.profileId;

        messagesListView = findViewById(R.id.messagesListView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        adapter = new ArrayAdapter<Message>(this, R.layout.item_message, R.id.messageText, messages) {
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.messageText);
                LinearLayout container = view.findViewById(R.id.messageContainer);
                LinearLayout root = (LinearLayout) view;

                Message msg = getItem(position);
                if (msg != null) {
                    textView.setText(msg.getText());

                    if (msg.getSenderId().equals(currentUserId)) {
                        root.setGravity(Gravity.END);
                        container.setBackgroundResource(R.drawable.bg_message_my);
                    } else {
                        root.setGravity(Gravity.START);
                        container.setBackgroundResource(R.drawable.bg_message);
                    }
                }
                return view;
            }
        };
        messagesListView.setAdapter(adapter);

        chatRef = FirebaseDatabase.getInstance().getReference("Chats").child(chatId);

        loadMessages();

        sendButton.setOnClickListener(v -> {
            String text = messageInput.getText().toString().trim();
            if (!text.isEmpty()) {
                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("senderId", currentUserId);
                messageMap.put("text", text);
                messageMap.put("timestamp", ServerValue.TIMESTAMP);

                chatRef.child("messages").push().setValue(messageMap);
                messageInput.setText("");
            }
        });

        chatRef.child("participants").child(currentUserId)
                .child("lastReadTimestamp").setValue(ServerValue.TIMESTAMP);
    }

    private void loadMessages() {
        chatRef.child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Message msg = child.getValue(Message.class);
                    if (msg != null) {
                        messages.add(msg);
                    }
                }
                adapter.notifyDataSetChanged();
                messagesListView.setSelection(messages.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
            }
        });
    }
}