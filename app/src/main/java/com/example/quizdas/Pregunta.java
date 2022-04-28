package com.example.quizdas;

public class Pregunta {

    private String pregunta;
    private String resp1;
    private String resp2;
    private String resp3;
    private  String respCorrecta;

    public Pregunta(String pPregunta, String pResp1, String pResp2, String pResp3, String pRespCorrecta){
        this.pregunta = pPregunta;
        this.resp1 = pResp1;
        this.resp2 =pResp2;
        this.resp3 = pResp3;
        this.respCorrecta = pRespCorrecta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public String getResp1() {
        return resp1;
    }

    public String getResp2() {
        return resp2;
    }

    public String getResp3() {
        return resp3;
    }

    public String getRespCorrecta() {
        return respCorrecta;
    }
}
