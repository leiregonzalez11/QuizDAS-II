package com.example.quizdas;

public class Usuario {

    private String nombre;
    private String tel;
    private String foto;
    private String email;
    private  String passwd;

    public Usuario(){}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String name) {
        this.nombre = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tlfno) {
        this.tel = tlfno;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String img) {
        this.foto = img;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mail) {
        this.email = mail;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String contra) {
        this.passwd = contra;
    }

}
