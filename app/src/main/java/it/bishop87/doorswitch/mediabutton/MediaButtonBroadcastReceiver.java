package it.bishop87.doorswitch.mediabutton;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.service.media.MediaBrowserService;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.bishop87.doorswitch.utility.PreferenceUtility;
import it.bishop87.doorswitch.utility.SettingsModel;

import static it.bishop87.doorswitch.utility.PreferenceUtility.checkDateTime;
import static it.bishop87.doorswitch.utility.PreferenceUtility.leggiNumeriAbilitati;

public class MediaButtonBroadcastReceiver extends BroadcastReceiver {
    private final static String TAG_MEDIA = "BroReceiver";

    private Context mContext;
    public MediaButtonBroadcastReceiver() {
        super ();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String intentAction = intent.getAction();

        Log.i (TAG_MEDIA, intentAction + " happended");

        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            Log.i (TAG_MEDIA, "no media button information");
            return;
        }

        KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            Log.i (TAG_MEDIA, "no keypress");
            return;
        }

        SettingsModel settings = PreferenceUtility.readSettings(context);
        int action = event.getAction();
        switch (action){
            case KeyEvent.ACTION_DOWN:
                Log.i (TAG_MEDIA, "ACTION_DOWN - porta aperta");
                if(checkDateTime(settings)){
                    sendSms("PORTA_APERTA", leggiNumeriAbilitati(context));
                }
                break;
            case KeyEvent.ACTION_UP:
                Log.i (TAG_MEDIA, "ACTION_UP - porta chiusa");
                if(checkDateTime(settings)){
                    sendSms("PORTA_CHIUSA", leggiNumeriAbilitati(context));
                }
                break;
            default:
                Log.i (TAG_MEDIA, "ACTION: " + action);
        }
    }

    private void sendSms(String msg, List<String> destNumbers) {
        if(destNumbers != null && !destNumbers.isEmpty() && !destNumbers.get(0).equals("") ){
            for (String number: destNumbers) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, msg, null, null);
                Log.d(TAG_MEDIA, "Message Sent [" + number + "]: " + msg);
            }
        }else{
            Toast.makeText(mContext.getApplicationContext(), "Nessun numero abilitato!", Toast.LENGTH_SHORT).show();
        }
    }

}
