package com.company;
import java.lang.Math;
public class SekwencjaPassCode {
    /*
     * Tworzy losowy passcode.
     * iloscZnakow - w zaleznosci od wybranego poziomu gry
     */

    public char[] losowanieSekwencji(int iloscZnakow){
        char[] klucz = new char[iloscZnakow];
        for(int i = 0; i < klucz.length; i++){
            int num = (int)((Math.random()*6) + 1); //losuje int  z przedzialu max 1-6 inclusive
            klucz[i] = zmianaKlucza(num);
        }
        return klucz;
    }
    //Zamiana wylosowanych cyfr na znaki
    public char zmianaKlucza(int number){
        char zmieniony = ' ';
        switch(number){ //zamien wybrany int 1-6 na odpowiedni char
            case 1: zmieniony = 'q';
                break;
            case 2: zmieniony = 'w';
                break;
            case 3: zmieniony = 'e';
                break;
            case 4: zmieniony = 'r';
                break;
            case 5: zmieniony = 't';
                break;
            case 6: zmieniony = 'y';
                break;
        }
        return zmieniony;
    }

}
