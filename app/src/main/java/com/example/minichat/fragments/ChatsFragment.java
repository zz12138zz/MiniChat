package com.example.minichat.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.minichat.R;
import com.example.minichat.adapter.UserAdapter;
import com.example.minichat.model.ChatList;
import com.example.minichat.model.Users;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
    private UserAdapter userAdapter;
    private List<Users> mUsers;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private List<ChatList> usersList;

    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_chats,container,false);
       recyclerView = view.findViewById(R.id.chats_recycle_view);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       usersList = new ArrayList<>();
       reference= FirebaseDatabase.getInstance().getReference("ChatList")
               .child(firebaseUser.getUid());
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               usersList.clear();
               for(DataSnapshot ss : snapshot.getChildren()){
                   ChatList chatList = ss.getValue(ChatList.class);
                   usersList.add(chatList);
               }
               listAllChats();

           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
       return view;
    }

    private void listAllChats(){
        //获得所有最近聊天对象
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot ss : snapshot.getChildren()) {
                    Users user = ss.getValue(Users.class);
                    for (ChatList chatList : usersList) {
                        if (user.getId().equals(chatList.getId())) {
                            mUsers.add(user);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(),mUsers,true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}