package it.bishop87.doorswitch.utility;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreferenceUtility {

    private static String NUMERI_ABILITATI = "NUMERI_ABILITATI";

    public static String SETTINGS_CHK_LUN = "SETTINGS_CHK_LUN";
    public static String SETTINGS_CHK_MAR = "SETTINGS_CHK_MAR";
    public static String SETTINGS_CHK_MER = "SETTINGS_CHK_MER";
    public static String SETTINGS_CHK_GIO = "SETTINGS_CHK_GIO";
    public static String SETTINGS_CHK_VEN = "SETTINGS_CHK_VEN";
    public static String SETTINGS_CHK_SAB = "SETTINGS_CHK_SAB";
    public static String SETTINGS_CHK_DOM = "SETTINGS_CHK_DOM";

    public static String SETTINGS_TIME_INIZIO = "SETTINGS_TIME_INIZIO";
    public static String SETTINGS_TIME_FINE   = "SETTINGS_TIME_FINE";

    public static List<String> leggiNumeriAbilitati(Context context) {
        List<String> array_list_numeri;
        String numeri = PreferenceManager.getDefaultSharedPreferences(context).getString(NUMERI_ABILITATI, "");

        array_list_numeri = new ArrayList<>(Arrays.asList(numeri.trim().split(";")));
        System.out.println("leggiNumeriAbilitati: " + array_list_numeri.toString());

        return array_list_numeri;
    }

    public static void scriviNumeriAbilitati(Context context, List<String> array_list_numeri){
        array_list_numeri.removeAll(Arrays.asList("", null));
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(PreferenceUtility.NUMERI_ABILITATI, TextUtils.join(";", array_list_numeri) ).apply();
        System.out.println("scriviNumeriAbilitati: " + TextUtils.join(";", array_list_numeri));
    }

    public static boolean numeroAbilitato(Context context, String numero, String prefisso) {
        if(numero.startsWith(prefisso)) {
            numero = numero.substring(prefisso.length());
        }
        System.out.println("numero senza prefisso [" + prefisso + "]: " + numero);
        List<String> numeri = leggiNumeriAbilitati(context);
        for(String n : numeri) {
            if(n.contains(numero))
                return true;
        }
        return false;
    }


    public static SettingsModel readSettings(Context context){
        SettingsModel settings = new SettingsModel();

        settings.setLun(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_CHK_LUN, false));
        settings.setMar(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_CHK_MAR, false));
        settings.setMer(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_CHK_MER, false));
        settings.setGio(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_CHK_GIO, false));
        settings.setVen(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_CHK_VEN, false));
        settings.setSab(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_CHK_SAB, false));
        settings.setDom(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_CHK_DOM, false));

        settings.setOraInizio(PreferenceManager.getDefaultSharedPreferences(context).getLong(SETTINGS_TIME_INIZIO, 0));
        settings.setOraFine(PreferenceManager.getDefaultSharedPreferences(context).getLong(SETTINGS_TIME_FINE, 0));

        return settings;
    }

    public static void writeSettings(Context context, SettingsModel settings){

        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(PreferenceUtility.SETTINGS_CHK_LUN, settings.isLun()).apply();
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(PreferenceUtility.SETTINGS_CHK_MAR, settings.isLun()).apply();
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(PreferenceUtility.SETTINGS_CHK_MER, settings.isLun()).apply();
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(PreferenceUtility.SETTINGS_CHK_GIO, settings.isLun()).apply();
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(PreferenceUtility.SETTINGS_CHK_VEN, settings.isLun()).apply();
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(PreferenceUtility.SETTINGS_CHK_SAB, settings.isLun()).apply();
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(PreferenceUtility.SETTINGS_CHK_DOM, settings.isLun()).apply();

        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putLong(PreferenceUtility.SETTINGS_TIME_INIZIO, settings.getOraInizio()).apply();
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putLong(PreferenceUtility.SETTINGS_TIME_FINE, settings.getOraFine()).apply();

    }

}
