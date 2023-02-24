package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import com.example.firebase.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference();

    HashMap<String,Integer> playerRecord = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityMainBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_main);


        /* Writing in Firebase */
        binding.write.setOnClickListener((View v)-> {
                if(binding.etPlayerName.getText()!=null && binding.etFreeKicks.getText()!=null){
                    playerRecord.put(binding.etPlayerName.getText().toString(),Integer.parseInt(binding.etFreeKicks.getText()+""));
                    ref.setValue(playerRecord);
                    Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
        });

        /* Reading from Firebase */
        binding.read.setOnClickListener((View v)-> {
                if(binding.etRead.getText()!=null){
                    DatabaseReference refToRead = ref.child(binding.etRead.getText().toString());
                    refToRead.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            binding.tvPlayerName.setText(binding.etRead.getText());
                            binding.tvFreeKicks.setText(snapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
        });

        /* Deleting from Firebase */
        binding.delete.setOnClickListener((View v)-> {
            if(binding.etRead.getText()!=null) {
                DatabaseReference refToDelete = ref.child(binding.etRead.getText().toString());
                refToDelete.removeValue();
                Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }
}