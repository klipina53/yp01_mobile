package com.example.yp01;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class singinscreen extends AppCompatActivity {

    EditText email;
    EditText password;
    String emailRegex = "[a-z0-9]+@[a-z0-9]+\\.[a-z]{1,3}";
    int countUser = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singinscreen);
    }

    public void AlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(singinscreen.this);
        builder.setTitle(title).setMessage(message).setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onLogin(View view){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        if(email.getText().length() == 0){
            AlertDialog("Уведомление", "Поле email не может быть пустым!");
            return;
        }
        if(password.getText().length() == 0){
            AlertDialog("Уведомление", "Поле password не может быть пустым!");
            return;
        }
        if(!email.getText().toString().trim().matches(emailRegex)){
            AlertDialog("Уведомление", "Неверный формат введенной почты\nПример: asd@asd.asd");
            return;
        }
        else{
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            Call<List<Users>> call = apiService.getUsers();
            call.enqueue(new Callback<List<Users>>() {
                @Override
                public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                    if (response.isSuccessful()) {
                        List<Users> items = response.body();
                        for (Users item: items) {
                            if(item.getEmail().equals(email.getText().toString()) && item.getPassword().equals(password.getText().toString()))
                            {
                                countUser++;
                                Intent intent = new Intent(singinscreen.this, mainscreen.class);
                                startActivity(intent);
                            }
                        }
                        if(countUser == 0){
                            AlertDialog("Уведомление", "Данный пользователь не найден!");
                            return;
                        }
                    } else {
                        return;
                    }
                }

                @Override
                public void onFailure(Call<List<Users>> call, Throwable t) {
                    return;
                }
            });
        }
    }
}
