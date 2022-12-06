package com.example.aplikasi_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListMhsActivity extends AppCompatActivity {
    List<Mahasiswa> fetchData;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    DatabaseReference databaseReference;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mhs);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchData = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance("https://praktikum12-39a52-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("mhs");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String nim = dataSnapshot.child("nim").getValue(String.class);
                    String nama = dataSnapshot.child("nama").getValue(String.class);
                    Mahasiswa data = new Mahasiswa(nim, nama);
                    fetchData.add(data);
                }
                myAdapter = new MyAdapter(fetchData);
                recyclerView.setAdapter(myAdapter);
                myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        String nim = fetchData.get(position).getNim();
                        startActivity(new Intent(ListMhsActivity.this, EditMhsActivity.class).putExtra("nim", nim));
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListMhsActivity.this, "Maaf terjadi kesalahan... coba beberapa saat lagi", Toast.LENGTH_SHORT).show();
            }
        });
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}