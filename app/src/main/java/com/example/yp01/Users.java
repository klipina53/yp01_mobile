package com.example.yp01;

public class Users {
    public int Id;
    public String Login;
    public String Email;
    public String Password;
    public String Telephone;
    public String Token;

    public int getId(){
        return Id;
    }

    public void setId(int Id){
        this.Id = Id;
    }

    public String getLogin(){
        return Login;
    }

    public void setLogin(String Login){
        this.Login = Login;
    }

    public String getEmail(){
        return Email;
    }

    public void setEmail(String Email){
        this.Email = Email;
    }

    public String getPassword(){
        return Password;
    }

    public void setPassword(String Password){
        this.Password = Password;
    }
    public String getTelephone(){
        return Telephone;
    }

    public void setTelephone(String Telephone){
        this.Telephone = Telephone;
    }

    public String getToken(){
        return Token;
    }

    public void setToken(String Token){
        this.Token = Token;
    }
}
