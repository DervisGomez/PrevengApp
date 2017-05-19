package org.app.prevengapp;

/**
 * Created by Administrador on 05/01/2017.
 */
public class GlobalService {

    private static GlobalService instance;


    // Direccion del servidor
    private String server;
    private Object usurio;

    private GlobalService(){}

    public static void setInstance(GlobalService instance) {
        GlobalService.instance = instance;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String fechaFormat(String fecha){
        String fechaFormat="";
        try {
            String anno=fecha.substring(0,4);
            String mes=fecha.substring(5,7);
            String dia=fecha.substring(8,10);
            int hora=Integer.valueOf(fecha.substring(11,13));
            String horaa;
            String min=fecha.substring(14,16);
            String tipo;
            if(hora>12){
                horaa=String.valueOf(hora-12);
                tipo="PM";
            }else{
                horaa=String.valueOf(hora);
                tipo="AM";
            }
            fechaFormat=dia+"-"+mes+"-"+anno+" "+horaa+":"+min+" "+tipo;
        }catch (Exception e){

        }

        return fechaFormat;
    }

    public static synchronized GlobalService getInstance(){
        if(instance==null){
            instance=new GlobalService();
        }
        return instance;
    }
}
