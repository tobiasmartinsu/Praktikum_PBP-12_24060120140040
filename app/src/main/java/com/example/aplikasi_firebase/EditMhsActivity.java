package com.example.aplikasi_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditMhsActivity extends AppCompatActivity {
    EditText etNIM, etNama, etJurusan, etAngkatan;
    Button btnSimpan, btnHapus;
    ImageButton btnBack;
    String temp_nim, temp_nama, temp_jurusan, temp_angkatan;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mhs);

        Intent intent = getIntent();
        String nim = intent.getStringExtra("nim");

        etNIM = findViewById(R.id.et_nim);
        etNama = findViewById(R.id.et_nama);
        etJurusan = findViewById(R.id.et_jurusan);
        etAngkatan = findViewById(R.id.et_angkatan);

        btnSimpan = findViewById(R.id.btn_simpan);
        btnHapus = findViewById(R.id.btn_hapus);
        btnBack = findViewById(R.id.btn_back);

        databaseReference = FirebaseDatabase.getInstance("https://praktikum12-39a52-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("mhs").child(nim);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    temp_nim = dataSnapshot.child("nim").getValue(String.class);
                    temp_nama = dataSnapshot.child("nama").getValue(String.class);
                    temp_jurusan = dataSnapshot.child("jurusan").getValue(String.class);
                    temp_angkatan = dataSnapshot.child("angkatan").getValue(String.class);

                    etNIM.setText(temp_nim);
                    etNama.setText(temp_nama);
                    etJurusan.setText(temp_jurusan);
                    etAngkatan.setText(temp_angkatan);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditMhsActivity.this, "Maaf terjadi kesalahan... coba beberapa saat lagi", Toast.LENGTH_SHORT).show();
            }
        });
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                finish();
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void deleteData() {
        databaseReference.removeValue();
        Toast.makeText(EditMhsActivity.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
    }

    private void updateData() {
        String nim = etNIM.getText().toString();
        String nama = etNama.getText().toString();
        String jurusan = etJurusan.getText().toString();
        String angkatan = etAngkatan.getText().toString();

        if (!nim.equals(temp_nim) || !nama.equals(temp_nama) || !jurusan.equals(temp_jurusan) || !angkatan.equals(temp_angkatan)) {
            Mahasiswa mahasiswa = new Mahasiswa(nim, nama, jurusan, angkatan);
            databaseReference.setValue(mahasiswa).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(EditMhsActivity.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                    etNIM.setText("");
                    etNama.setText("");
                    etJurusan.setText("");
                    etAngkatan.setText("");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    //apabila input data gagal maka akan melakukan hal berikut
                    Toast.makeText(EditMhsActivity.this, "Data Gagal diubah", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(EditMhsActivity.this, "Data tidak ada yang diubah", Toast.LENGTH_SHORT).show();
        }
    }
}