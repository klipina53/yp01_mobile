package com.example.yp01;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class singupscreen extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText name;
    EditText phone;
    String emailRegex = "([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)";
    String nameRegex = "^([А-ЯA-Z]|[А-ЯA-Z][\\x27а-яa-z]{1,}|[А-ЯA-Z][\\x27а-яa-z]{1,}\\-([А-ЯA-Z][\\x27а-яa-z]{1,}|(оглы)|(кызы)))\\040[А-ЯA-Z][\\x27а-яa-z]{1,}(\\040[А-ЯA-Z][\\x27а-яa-z]{1,})?$";
    String phoneRegex = "^(?![01234569])((\\+7|8)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singupscreen);
    }

    public void AlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(singupscreen.this);
        builder.setTitle(title).setMessage(message).setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onRegin(View view){
        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        name = findViewById(R.id.fullname_reg);
        phone = findViewById(R.id.phonenumber_reg);
        if(email.getText().length() == 0){
            AlertDialog("Уведомление", "Поле email не может быть пустым!");
            return;
        }
        if(password.getText().length() == 0){
            AlertDialog("Уведомление", "Поле password не может быть пустым!");
            return;
        }
        if(name.getText().length() == 0){
            AlertDialog("Уведомление", "Поле full name не может быть пустым!");
            return;
        }
        if(phone.getText().length() == 0){
            AlertDialog("Уведомление", "Поле phone number не может быть пустым!");
            return;
        }
        if(!email.getText().toString().trim().matches(emailRegex)){
            AlertDialog("Уведомление", "Неверный формат введенной почты\nПример: asd@asd.asd");
            return;
        }
        if(!name.getText().toString().trim().matches(nameRegex)){
            AlertDialog("Уведомление", "Неверный формат введенного имени\nПример: Тест Тест Тест");
            return;
        }
        if(!phone.getText().toString().trim().matches(phoneRegex)){
            AlertDialog("Уведомление", "Неверный формат введенного номера телефона\nПример: +7/81111111111");
            return;
        }
        else{
            registerUser();
            Intent intent = new Intent(this, mainscreen.class);
            startActivity(intent);
        }
    }

    private void registerUser() {
        String nameReg = name.getText().toString();
        String passwordReg = password.getText().toString();
        String emailReg = email.getText().toString();
        String phoneReg = phone.getText().toString();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Users> call = apiService.regin(nameReg, passwordReg, emailReg, phoneReg);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    Users user = response.body();
                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                return;
            }
        });
    }
    public void onCancel(View view){
        Intent intent = new Intent(this, singinscreen.class);
        startActivity(intent);
    }
}