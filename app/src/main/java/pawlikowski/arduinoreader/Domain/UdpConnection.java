package pawlikowski.arduinoreader.Domain;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


/**
 * Created by Sebas on 08.01.2018.
 */

public class UdpConnection extends AsyncTask<String, byte[], Boolean> {



    int server_port = 8888;
    DatagramSocket diagramSocket;
    InetAddress local;
    DatagramPacket diagramPacket;
    String data;



    protected void onPreExecute() {
    }


    public Boolean doInBackground(String... parameter) {

        boolean result = false;
        byte[] buf = parameter[0].getBytes();

        try {
            diagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            local = InetAddress.getByName("192.168.0.4");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        diagramPacket = new DatagramPacket(buf, buf.length, local, server_port);
        try {
            diagramSocket.send(diagramPacket);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }

            try {
                diagramSocket.receive(diagramPacket);
                data = new String(diagramPacket.getData());

            } catch (IOException e) {

            }



        return result;
    }


    @Override
    protected void onCancelled() {

    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Log.v("dd", "onPostExecute: Completed with an Error.");
        } else {
            Log.v("dd", "onPostExecute: Completed.");

                String[] datas = data.split(";");
                String temperature = datas[0];
                String humidty = datas[1];

        }

    }
    @Override
    protected void onProgressUpdate(byte[]... values) {
    }


}
