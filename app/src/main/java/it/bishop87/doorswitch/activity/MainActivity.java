package it.bishop87.doorswitch.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import it.bishop87.doorswitch.utility.PreferenceUtility;
import it.bishop87.doorswitch.R;
import it.bishop87.doorswitch.utility.SettingsModel;

import static it.bishop87.doorswitch.utility.PreferenceUtility.checkDateTime;

public class MainActivity extends Activity {

    private List<String> array_list_numeri;
    private ArrayAdapter<String> arrayAdapter;
    private TextView txtOrarioInizio;
    private TextView txtOrarioFine;

    private CheckBox chkLun;
    private CheckBox chkMar;
    private CheckBox chkMer;
    private CheckBox chkGio;
    private CheckBox chkVen;
    private CheckBox chkSab;
    private CheckBox chkDom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chkLun = findViewById(R.id.chkLun);
        chkMar = findViewById(R.id.chkMar);
        chkMer = findViewById(R.id.chkMer);
        chkGio = findViewById(R.id.chkGio);
        chkVen = findViewById(R.id.chkVen);
        chkSab = findViewById(R.id.chkSab);
        chkDom = findViewById(R.id.chkDom);

        txtOrarioInizio = findViewById(R.id.txtOrarioInizio);
        txtOrarioFine = findViewById(R.id.txtOrarioFine);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ListView lvNumeriAbilitati = findViewById(R.id.lvNumeriAbilitati);

        array_list_numeri = PreferenceUtility.leggiNumeriAbilitati(MainActivity.this);
        arrayAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.row, array_list_numeri);

        lvNumeriAbilitati.setAdapter(arrayAdapter);
        lvNumeriAbilitati.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.dlg_delete_number_title)
                        .setMessage(R.string.dlg_delete_number_message)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                array_list_numeri.remove(position);
                                salvaListaNumeri();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

                return false;
            }
        });

        SettingsModel settings = PreferenceUtility.readSettings(MainActivity.this);
        chkLun.setChecked(settings.isLun());
        chkMar.setChecked(settings.isMar());
        chkMer.setChecked(settings.isMer());
        chkGio.setChecked(settings.isGio());
        chkVen.setChecked(settings.isVen());
        chkSab.setChecked(settings.isSab());
        chkDom.setChecked(settings.isDom());

        txtOrarioInizio.setText(settings.getOrarioInizioAsString());
        txtOrarioFine.setText(settings.getOrarioFineAsString());
    }

    public void btnAggiungiNumeroAbilitatoClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog.setTitle(R.string.dlg_add_number_title);

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        dialog.setView(input);

        // Set up the buttons
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                getResources().getString(R.string.dlg_add_number_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String numero = input.getText().toString();
                        if(!numero.isEmpty()) {
                            array_list_numeri.add(numero);
                            salvaListaNumeri();
                        }
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                getResources().getString(R.string.dlg_add_number_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        dialog.show();
    }

    private void salvaListaNumeri(){
        PreferenceUtility.scriviNumeriAbilitati(MainActivity.this, array_list_numeri);
        arrayAdapter.notifyDataSetChanged();
    }

    public void btnOrarioInizioClick(View view) {
        SettingsModel settings = PreferenceUtility.readSettings(this);
        Calendar mcurrentTime = Calendar.getInstance();
        mcurrentTime.setTimeInMillis(settings.getOraInizio());
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                txtOrarioInizio.setText( String.format(Locale.getDefault(),"%02d:%02d", selectedHour, selectedMinute));

                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, selectedHour);
                c.set(Calendar.MINUTE, selectedMinute);
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit()
                        .putLong(PreferenceUtility.SETTINGS_TIME_INIZIO, c.getTimeInMillis()).apply();
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Seleziona Orario Inizio");
        mTimePicker.show();
    }

    public void btnOrarioFineClick(View view) {
        SettingsModel settings = PreferenceUtility.readSettings(this);
        Calendar mcurrentTime = Calendar.getInstance();
        mcurrentTime.setTimeInMillis(settings.getOraFine());
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                txtOrarioFine.setText( String.format(Locale.getDefault(),"%02d:%02d", selectedHour, selectedMinute));

                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, selectedHour);
                c.set(Calendar.MINUTE, selectedMinute);
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit()
                        .putLong(PreferenceUtility.SETTINGS_TIME_FINE, c.getTimeInMillis()).apply();

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Seleziona Orario Fine");
        mTimePicker.show();
    }

    public void onChkGiornoClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.chkLun:
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit()
                        .putBoolean(PreferenceUtility.SETTINGS_CHK_LUN, checked).apply();
                break;
            case R.id.chkMar:
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit()
                        .putBoolean(PreferenceUtility.SETTINGS_CHK_MAR, checked).apply();
                break;
            case R.id.chkMer:
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit()
                        .putBoolean(PreferenceUtility.SETTINGS_CHK_MER, checked).apply();
                break;
            case R.id.chkGio:
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit()
                        .putBoolean(PreferenceUtility.SETTINGS_CHK_GIO, checked).apply();
                break;
            case R.id.chkVen:
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit()
                        .putBoolean(PreferenceUtility.SETTINGS_CHK_VEN, checked).apply();
                break;
            case R.id.chkSab:
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit()
                        .putBoolean(PreferenceUtility.SETTINGS_CHK_SAB, checked).apply();
                break;
            case R.id.chkDom:
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit()
                        .putBoolean(PreferenceUtility.SETTINGS_CHK_DOM, checked).apply();
                break;
        }
    }

    public void btnTestOrarioClick(View view) {
        String now = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());
        SettingsModel settings = PreferenceUtility.readSettings(this);
        String compresa = checkDateTime(settings)?" compresa ":" non compresa ";
        String intervallo = settings.getOrarioInizioAsString() + " - " + settings.getOrarioFineAsString();
        Toast.makeText(this, "Ora corrente: " + now + compresa +"nell'intervallo " + intervallo ,Toast.LENGTH_SHORT).show();
    }
}
