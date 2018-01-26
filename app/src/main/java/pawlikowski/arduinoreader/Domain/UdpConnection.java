package pawlikowski.arduinoreader.Domain;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import java.util.Set;

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
        final Button button = (Button) ((Activity) _context).findViewById(R.id.ConnectButton);
        if(button.isEnabled()){
            button.post(new Runnable() {
                @Override
                public void run() {
                    button.setEnabled(false);
                }
            });
            final TextView statu = (TextView) ((Activity) _context).findViewById(R.id.stat);
            statu.post(new Runnable() {
                @Override
                public void run() {
                    statu.setText("");
                    statu.setText("ENABLED");
                    statu.setTextColor(Color.GREEN);
                }
            });
        }
        boolean result = false;
        byte[] buf = parameter[0].getBytes();
        byte[] newBuf = Arrays.copyOf(buf,10);
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
                String[] datas = data.split("-");
                TextView vtemp = (TextView) ((Activity) _context).findViewById(R.id.temp);
                TextView vhum = (TextView) ((Activity) _context).findViewById(R.id.hum);
                SetVisibility(vtemp,View.INVISIBLE);
                SetVisibility(vhum,View.INVISIBLE);
                if(temp){
                    if(hum){
                        SetVisibility(vtemp,View.VISIBLE);
                        SetVisibility(vhum,View.VISIBLE);

                         temperature = datas[0].replaceAll("\\D.+","");
                         SetText(vtemp,"Temp : " + temperature + "C");
                        //vtemp.setText("Tem : " + temperature + "C");
                         humidty = datas[1].replaceAll("\\D.+","");
                         SetText(vhum,"Hum : " + humidty + "%");
                    //     vhum.setText("Hum : " + humidty + "%");
                    }
                    else {
                        SetVisibility(vtemp,View.VISIBLE);

                        temperature = datas[0].replaceAll("\\D.+","");
                     //   vtemp.setText(temperature);
                        SetText(vtemp,"Temp : " + temperature + "C");
                    }
                }
                else{
                    if(hum){
                        SetVisibility(vhum,View.VISIBLE);
                        humidty = datas[1].replaceAll("\\D.+","");
                      //  vhum.setText(humidty);
                        SetText(vhum,"Temp : " + humidty + "%");
                    }
                }



            } catch (IOException e) {

            }



        return result;
    }

    private void SetVisibility(final TextView textView, final int visibility){
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setVisibility(visibility);
            }
        });
    }

    private void SetText(final TextView textView, final String text){
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(text);
            }
        });
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
