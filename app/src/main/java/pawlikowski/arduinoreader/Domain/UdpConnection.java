package pawlikowski.arduinoreader.Domain;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import pawlikowski.arduinoreader.R;


/**
 * Created by Sebas on 08.01.2018.
 */

public class UdpConnection extends AsyncTask<String, byte[], Boolean> {

    Context _context;
public UdpConnection(Context context){
   _context = context;
}

    int server_port = 8888;
    DatagramSocket diagramSocket;
    InetAddress local;
    DatagramPacket diagramPacket;
    String data;



    protected void onPreExecute() {
    }


    public Boolean doInBackground(String... parameter) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
        String ipAddress = prefs.getString("prefKeyArduinoAddress", "");

        boolean result = false;
        byte[] buf = parameter[0].getBytes();
        byte[] newBuf = Arrays.copyOf(buf,5);
        try {
            diagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            local = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        diagramPacket = new DatagramPacket(newBuf, newBuf.length, local, server_port);
        try {
            diagramSocket.send(diagramPacket);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }

            try {
                diagramSocket.receive(diagramPacket);
                data = new String(diagramPacket.getData());
                boolean temp = prefs.getBoolean("prefKeyTrackTemperature", true);
                boolean hum =prefs.getBoolean("prefKeyTrackHumidity", true);
                String temperature;
                String humidty;
                String[] datas = data.split(";");
                TextView vtemp = (TextView) ((Activity) _context).findViewById(R.id.temp);
                TextView vhum = (TextView) ((Activity) _context).findViewById(R.id.hum);
                vtemp.setVisibility(View.INVISIBLE);
                vhum.setVisibility(View.INVISIBLE);
                if(temp){
                    if(hum){
                        vtemp.setVisibility(View.VISIBLE);
                        vhum.setVisibility(View.VISIBLE);

                         temperature = datas[0].replaceAll("\\D+","");
                        vtemp.setText(temperature);
                         humidty = datas[1].replaceAll("\\D+","");
                         vhum.setText(humidty);
                    }
                    else {
                        vtemp.setVisibility(View.VISIBLE);
                        temperature = datas[0].replaceAll("\\D+","");
                        vtemp.setText(temperature);
                    }
                }
                else{
                    if(hum){
                        vhum.setVisibility(View.VISIBLE);
                        humidty = datas[1].replaceAll("\\D+","");
                        vhum.setText(humidty);
                    }
                }



            } catch (IOException e) {

            }



        return result;
    }


    @Override
    protected void onCancelled() {

    }

    @Override
    protected void onPostExecute(Boolean result) {
   

    }
    @Override
    protected void onProgressUpdate(byte[]... values) {
    }


}
