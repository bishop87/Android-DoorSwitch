package it.bishop87.doorswitch.utility;


import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SettingsModel {

    private boolean lun;
    private boolean mar;
    private boolean mer;
    private boolean gio;
    private boolean ven;
    private boolean sab;
    private boolean dom;

    private long oraInizio;
    private long oraFine;

    public boolean isLun() {
        return lun;
    }

    public void setLun(boolean lun) {
        this.lun = lun;
    }

    public boolean isMar() {
        return mar;
    }

    public void setMar(boolean mar) {
        this.mar = mar;
    }

    public boolean isMer() {
        return mer;
    }

    public void setMer(boolean mer) {
        this.mer = mer;
    }

    public boolean isGio() {
        return gio;
    }

    public void setGio(boolean gio) {
        this.gio = gio;
    }

    public boolean isVen() {
        return ven;
    }

    public void setVen(boolean ven) {
        this.ven = ven;
    }

    public boolean isSab() {
        return sab;
    }

    public void setSab(boolean sab) {
        this.sab = sab;
    }

    public boolean isDom() {
        return dom;
    }

    public void setDom(boolean dom) {
        this.dom = dom;
    }

    public long getOraInizio() {
        return oraInizio;
    }

    public String getOrarioInizioAsString() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(oraInizio);

        return String.format(Locale.getDefault(),"%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }

    public int getOraInizioAsInt(){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(oraInizio);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinutiInizioAsInt(){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(oraInizio);

        return c.get(Calendar.MINUTE);
    }

    public void setOraInizio(long oraInizio) {
        this.oraInizio = oraInizio;
    }

    public long getOraFine() {
        return oraFine;
    }

    public String getOrarioFineAsString() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(oraFine);

        return String.format(Locale.getDefault(),"%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }

    public int getOraFineAsInt(){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(oraFine);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinutiFineAsInt(){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(oraFine);

        return c.get(Calendar.MINUTE);
    }

    public void setOraFine(long oraFine) {
        this.oraFine = oraFine;
    }


    public boolean[] getDaysArray() {
        //array: dom, lun, mar, mer, gio, ven, sab
        return new boolean[]{ isDom(), isLun(), isMar(), isMer(), isGio(), isVen(), isSab() };
    }

}
