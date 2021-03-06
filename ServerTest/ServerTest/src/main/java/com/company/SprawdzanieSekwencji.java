package com.company;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class SprawdzanieSekwencji {

    private int poprawnaLiteraIPozycja = 0;
    private int porawnaTylkoLitera = 0;
    private char[] sprawdzanieOdpowiedzi;
    private final String WYGRYWAJACA_SEKWENCJA;


     ///Ustala dlugosc wygranego kodu
    public SprawdzanieSekwencji(String newPasscode){
        String s = "";
        for(int i = 0; i < newPasscode.length(); i++)
            s += "B";
        WYGRYWAJACA_SEKWENCJA = s;
    }

    public char[] sprawdzOdpowiedz(String passcode, String odpowiedz){
        char[] passcodeArr = passcode.toCharArray();
        char[] odpArr = odpowiedz.toCharArray();

        for(int i = 0; i < passcodeArr.length; i++){  //sprawdzenie poprawnej litery i pozycji
            if(odpArr[i] == passcodeArr[i]){
                //to zamiana odpArr[i] i passcodeArr[i]
                passcodeArr[i] = 'k';
                odpArr[i] = 'g';
                poprawnaLiteraIPozycja++;
            }
        }
        for(int i = 0; i < odpowiedz.length(); i++){  //sprawdzanie tylko poprawnej litery - bez ustalania pozycji
            for(int j = 0; j < passcodeArr.length; j++){
                if(odpArr[i] == passcodeArr[j]){
                    passcodeArr[j] = 'k';
                    porawnaTylkoLitera++;
                    break;
                }
            }
        }
        return utworzSekwencjeZwrotna(poprawnaLiteraIPozycja + porawnaTylkoLitera);
    }

    /*
     * generuje odpowiedz serwera reprezentujaca wyniki odgadniecia
     * b- odgadnieto tylko litere
     * B- odganieta pozycje i litere
     * dlugosc odpopwiedzi zalezna od poziolmu trudnosci
     */
    public char[] utworzSekwencjeZwrotna(int sprawdzanieDlugosciOdpowiedzi){
        sprawdzanieOdpowiedzi = new char[sprawdzanieDlugosciOdpowiedzi];
        for(int i = 0; i < sprawdzanieOdpowiedzi.length; i++){
            if(poprawnaLiteraIPozycja > 0){
                sprawdzanieOdpowiedzi[i] = 'B';
                poprawnaLiteraIPozycja--;
            }
            else{
                sprawdzanieOdpowiedzi[i] = 'b';
                porawnaTylkoLitera--;
            }
        }
        return sprawdzanieOdpowiedzi;
    }

    /*
     * Sprawdzanie czy wygrana - sprawdzanieOdpowiedzi musi byc rowne BBB or BBBB or  BBBB or BBBBBB
     */
    public boolean sprawdzCzyWygrana(){
        if(WYGRYWAJACA_SEKWENCJA.equals(String.valueOf(sprawdzanieOdpowiedzi)))
            return true;
        return false;
    }


}
