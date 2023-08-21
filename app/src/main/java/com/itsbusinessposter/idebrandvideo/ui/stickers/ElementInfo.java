package com.itsbusinessposter.idebrandvideo.ui.stickers;

import android.graphics.Bitmap;
import android.net.Uri;

public class ElementInfo {
    private Bitmap BITMAP;
    private String COLORTYPE;
    private int COMP_ID;
    private String FIELD_FOUR = "";
    int FIELD_ONE = 0;
    private String FIELD_THREE = "";
    private String FIELD_TWO = "";
    private int HEIGHT;
    private int ORDER;
    private float POS_X;
    private float POS_Y;
    private String RES_ID;
    private Uri RES_URI;
    private float ROTATION;
    private int STC_COLOR;
    private int STC_HUE;
    private int STC_OPACITY;
    private String STKR_PATH = "";
    int ScaleProg;
    private int TEMPLATE_ID;
    private String TYPE = "";
    private int WIDTH;
    int XRotateProg;
    int YRotateProg;
    private float Y_ROTATION;
    int ZRotateProg;

    public ElementInfo(int i, float f, float f2, int i2, int i3, float f3, float f4, String str, String str2, int i4, int i5, int i6, int i7, int i8, int i9, int i10, String str3, String str4, int i11, int i12, String str5, String str6, String str7, Uri uri, Bitmap bitmap) {
        this.TEMPLATE_ID = i;
        this.POS_X = f;
        this.POS_Y = f2;
        this.WIDTH = i2;
        this.HEIGHT = i3;
        this.ROTATION = f3;
        this.Y_ROTATION = f4;
        this.RES_ID = str;
        this.RES_URI = uri;
        this.BITMAP = bitmap;
        this.TYPE = str2;
        this.ORDER = i4;
        this.STC_COLOR = i5;
        this.COLORTYPE = str4;
        this.STC_OPACITY = i6;
        this.XRotateProg = i7;
        this.YRotateProg = i8;
        this.ZRotateProg = i9;
        this.ScaleProg = i10;
        this.STKR_PATH = str3;
        this.STC_HUE = i11;
        this.FIELD_ONE = i12;
        this.FIELD_TWO = str5;
        this.FIELD_THREE = str6;
        this.FIELD_FOUR = str7;
    }

    public ElementInfo() {
    }

    public int getCOMP_ID() {
        return this.COMP_ID;
    }

    public void setCOMP_ID(int i) {
        this.COMP_ID = i;
    }

    public int getTEMPLATE_ID() {
        return this.TEMPLATE_ID;
    }

    public void setTEMPLATE_ID(int i) {
        this.TEMPLATE_ID = i;
    }

    public float getPOS_X() {
        return this.POS_X;
    }

    public void setPOS_X(float f) {
        this.POS_X = f;
    }

    public float getPOS_Y() {
        return this.POS_Y;
    }

    public void setPOS_Y(float f) {
        this.POS_Y = f;
    }

    public String getRES_ID() {
        return this.RES_ID;
    }

    public void setRES_ID(String str) {
        this.RES_ID = str;
    }

    public String getTYPE() {
        return this.TYPE;
    }

    public void setTYPE(String str) {
        this.TYPE = str;
    }

    public int getORDER() {
        return this.ORDER;
    }

    public void setORDER(int i) {
        this.ORDER = i;
    }

    public float getROTATION() {
        return this.ROTATION;
    }

    public void setROTATION(float f) {
        this.ROTATION = f;
    }

    public int getWIDTH() {
        return this.WIDTH;
    }

    public void setWIDTH(int i) {
        this.WIDTH = i;
    }

    public int getHEIGHT() {
        return this.HEIGHT;
    }

    public void setHEIGHT(int i) {
        this.HEIGHT = i;
    }

    public float getY_ROTATION() {
        return this.Y_ROTATION;
    }

    public void setY_ROTATION(float f) {
        this.Y_ROTATION = f;
    }

    public Uri getRES_URI() {
        return this.RES_URI;
    }

    public void setRES_URI(Uri uri) {
        this.RES_URI = uri;
    }

    public Bitmap getBITMAP() {
        return this.BITMAP;
    }

    public void setBITMAP(Bitmap bitmap) {
        this.BITMAP = bitmap;
    }

    public int getSTC_COLOR() {
        return this.STC_COLOR;
    }

    public void setSTC_COLOR(int i) {
        this.STC_COLOR = i;
    }

    public String getCOLORTYPE() {
        return this.COLORTYPE;
    }

    public void setCOLORTYPE(String str) {
        this.COLORTYPE = str;
    }

    public int getSTC_OPACITY() {
        return this.STC_OPACITY;
    }

    public void setSTC_OPACITY(int i) {
        this.STC_OPACITY = i;
    }

    public int getXRotateProg() {
        return this.XRotateProg;
    }

    public void setXRotateProg(int i) {
        this.XRotateProg = i;
    }

    public int getYRotateProg() {
        return this.YRotateProg;
    }

    public void setYRotateProg(int i) {
        this.YRotateProg = i;
    }

    public int getZRotateProg() {
        return this.ZRotateProg;
    }

    public void setZRotateProg(int i) {
        this.ZRotateProg = i;
    }

    public int getScaleProg() {
        return this.ScaleProg;
    }

    public void setScaleProg(int i) {
        this.ScaleProg = i;
    }

    public int getFIELD_ONE() {
        return this.FIELD_ONE;
    }

    public void setFIELD_ONE(int i) {
        this.FIELD_ONE = i;
    }

    public String getFIELD_TWO() {
        return this.FIELD_TWO;
    }

    public void setFIELD_TWO(String str) {
        this.FIELD_TWO = str;
    }

    public String getFIELD_THREE() {
        return this.FIELD_THREE;
    }

    public void setFIELD_THREE(String str) {
        this.FIELD_THREE = str;
    }

    public String getFIELD_FOUR() {
        return this.FIELD_FOUR;
    }

    public void setFIELD_FOUR(String str) {
        this.FIELD_FOUR = str;
    }

    public String getSTKR_PATH() {
        return this.STKR_PATH;
    }

    public void setSTKR_PATH(String str) {
        this.STKR_PATH = str;
    }

    public int getSTC_HUE() {
        return this.STC_HUE;
    }

    public void setSTC_HUE(int i) {
        this.STC_HUE = i;
    }
}
