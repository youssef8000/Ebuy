package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ResetActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        TextView reset_user=findViewById(R.id.user_name_reset);
        EditText pass=findViewById(R.id.passwordtype);
        EditText repass=findViewById(R.id.repassword);
        Button confirmbtn=findViewById(R.id.confirm_Password);
        DatabaseHelper db =new DatabaseHelper(this);
        Intent intent =getIntent();
        reset_user.setText(intent.getStringExtra("email"));
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=reset_user.getText().toString();
                String password=pass.getText().toString();
                String repassword=repass.getText().toString();
                if(password.equals(repassword)){
                    Boolean checkuser_upate=db.updatePassword(user,password);
                    if(checkuser_upate==true){
                        Intent intent=new Intent(getApplicationContext(), loginActivity .class);
                        startActivity(intent);
                        Toast.makeText(ResetActivity.this, "Password updated", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(ResetActivity.this, "Password not updated", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ResetActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}