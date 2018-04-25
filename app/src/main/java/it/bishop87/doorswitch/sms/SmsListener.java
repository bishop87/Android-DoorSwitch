package it.bishop87.doorswitch.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import it.bishop87.doorswitch.utility.BatteryUtility;
import it.bishop87.doorswitch.utility.PreferenceUtility;

public class SmsListener extends BroadcastReceiver {

    private static final String PREFISSO_ITA = "+39";
    private enum Azioni {
        STATUS,
        UNKNOWN
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();  //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msgFrom;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msgFrom = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();

                        Log.d("SmsListener","msgFrom: " + msgFrom);
                        Log.d("SmsListener","msgBody: " + msgBody);

                        if(PreferenceUtility.numeroAbilitato(context, msgFrom, PREFISSO_ITA)){

                            switch(Azioni.valueOf(msgBody.toUpperCase())){
                                case STATUS:
                                    Log.d("SmsListener","Messaggio riconosciuto: abortBroadcast");
                                    sendStatusSMS(context, msgFrom);
                                    abortBroadcast();
                                    break;
                                case UNKNOWN:
                                default: Log.d("SmsListener","Messaggio non riconosciuto: no abortBroadcast");
                            }

                        }
                        else{
                            Log.d("SmsListener","Mittente non riconosciuto: no abortBroadcast");
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendStatusSMS(final Context context, final String phoneNo) {
        String msg = "status: ok\nbattery: " + BatteryUtility.getBatteryPercentage(context);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        Log.d("SmsListener", "Message Sent [" + phoneNo + "]: " + msg);
    }

}
