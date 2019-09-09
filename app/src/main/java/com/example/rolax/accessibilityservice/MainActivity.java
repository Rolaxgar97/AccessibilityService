package com.example.rolax.accessibilityservice;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences prefere;
    captured objCaptured=new captured();

    public static Context con=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefere= getSharedPreferences("bandera",Context.MODE_PRIVATE);
        int bandera=Integer.valueOf(prefere.getString("bandera","0"));
        con=this;

        if(bandera==0)
        alerta();
    }


    void alerta(){

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Para que esta aplicacion pueda ofrecerle un mejor servicio se debe de activar un servicio" +
                "con el mismo nombre");


        builder.setPositiveButton("Ir a la configuracion", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent2 = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivityForResult(intent2,0);
            }
        });

        builder.setNegativeButton("Cancelar",null);
        Dialog dialogo=builder.create();
        dialogo.show();
    }
}
