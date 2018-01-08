package pawlikowski.arduinoreader.Domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Timer;
import java.util.TimerTask;

import pawlikowski.arduinoreader.MainActivity;

/**
 * Created by Sebas on 08.01.2018.
 */

public class BackGroundTimerTask extends TimerTask {
    private Context _context;
    public BackGroundTimerTask(Context context ){
        _context = context;
    }

    @Override
    public void run() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
// then you use
        boolean temp = prefs.getBoolean("prefKeyTrackTemperature", true);
        boolean hum =prefs.getBoolean("prefKeyTrackHumidity", true);

        String buf = "";

        if(temp){
            buf+="T";
        }
        if(hum){
            buf+="H";
        }
        new UdpConnection().execute(buf,null,null);
    }
}
