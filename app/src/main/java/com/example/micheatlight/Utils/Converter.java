package com.example.micheatlight.Utils;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class Converter {
    private int maxZeichen;
    private int maxBenachrichtigungen;
    private static final String TAG="Converter";

    //Konstruktor
    public Converter(int maxZeichen, int maxBenachrichtigungen) {
        this.maxZeichen = maxZeichen;
        this.maxBenachrichtigungen = maxBenachrichtigungen;
    }

    //getter
    public int getMaxZeichen() {
        return maxZeichen;
    }

    //getter
    public int getMaxBenachrichtigungen() {
        return maxBenachrichtigungen;
    }

    //Testet, ob die Nachricht gesendet werden kann
    public boolean islegal(String str) {
        boolean toReturn = true;
        Charset UTF8_CHARSET = Charset.forName("UTF-8");
        byte[] abc = str.getBytes(UTF8_CHARSET);
        int length = abc.length;
        if (!(length <= this.maxZeichen)) {
            toReturn = false;
        }
        return toReturn;
    }

    //Testet, ob das Maximallimit erreicht wird (this.maxBenachrichtigungen)
    public boolean isMultiLegal(String str) {
        boolean toReturn = true;
        Charset UTF8_CHARSET = Charset.forName("UTF-8");
        byte[] abc = str.getBytes(UTF8_CHARSET);
        int length = abc.length;
        if (!(length <= (this.maxZeichen * this.maxBenachrichtigungen))) {
            toReturn = false;
            Log.e(TAG,"MultiLegal is greater than "+(this.maxZeichen*this.maxBenachrichtigungen));
        }
        return toReturn;
    }

    //Convertiert array zu String
    public String convertSingleArrayToString(String[] strArr) {
        String toReturn="";
        for (int i = 0; i < strArr.length; i++) {
            toReturn+=strArr[i]+"; ";
        }
        return toReturn;
    }

    //Gibt zusammenhängenden Vokabelstring aus
    public String convertMultiArrayToString(String[] strArr1, String[] strArr2) {
        String toReturn="";
        if(strArr1.length!=strArr2.length) {
            Log.e(TAG, "convertMultiArrayToString: ", new IllegalArgumentException("strArr1 and strArr2 don't have the same lenght, strArr1("+strArr1.length+") != strArr2("+strArr2.length+")"));
        }
        for (int i = 0; i < strArr1.length; i++) {
            toReturn+=strArr1[i]+"-"+strArr2[i]+";";
        }
        return toReturn;
    }

    //Konvertiert ein String array zu einem Int array
    public int[] stringArrayToIntArray(String[] strArr) {
        int size=strArr.length;
        int[] arr=new int[size];
        //Array wird durchlaufen, jeder einzelne Wert wird konvertiert
        for (int i = 0; i < size; i++) {
            arr[i]= Integer.parseInt(strArr[i]);
        }
        return arr;
    }

    //Teilt String in [maxZeichen] große Byteblöcke auf
    //Geklaut von StackOverflow: https://stackoverflow.com/questions/520907/split-java-string-in-chunks-of-1024-bytes
    public String[] chunk_split(String original) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(original.getBytes());
        int n = 0;
        int length = this.maxZeichen;
        String seperator = ";#;\n";
        if(original.contains(seperator)) {
            Log.e(TAG, "chunk_split: seperatorAllreadyInString");
        }
        byte[] buffer = new byte[length];
        String result = "";
        while ((n = bis.read(buffer)) > 0) {
            for (byte b : buffer) {
                result += (char) b;
            }
            Arrays.fill(buffer, (byte) 0);
            result += seperator;
        }
        String[] returnStrArr = result.split(seperator);
        return returnStrArr;
    }

    public String convertToLegal(String original) throws IOException {
        String[] toReturn=chunk_split(original);
        return toReturn[0];
    }
}

