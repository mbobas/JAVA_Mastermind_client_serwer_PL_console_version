package com.company;

public class SprawdzenieWejscia {

    /*
     Sprawdzenie czy wybrano poprawny poziom
     */
    public int poprawnyPoziomSprawdzenie(String poziom){
        switch(poziom){
            case "e":
                return 3;
            case "n":
                return 4;
            case "h":
                return 5;
            case "m":
                return 6;
            default:
                return 0;
        }
    }

    /*
    Sprawdzenie poprawnosci odpowiedzi
     */
    public boolean poprawnaOdpowiedzSprawdzenie(String odpowiedzKlienta, int dlugoscKoduDoSprawdzenia){
        if(quit(odpowiedzKlienta))
            return true;
        if(odpowiedzKlienta.length() == dlugoscKoduDoSprawdzenia)
            for(int i = 0; i < odpowiedzKlienta.length(); i++) //sprawdzenie czy kazdy znak odpowiada q,w,e,r,t, lub y
                switch(odpowiedzKlienta.charAt(i)){
                    case 'q':
                    case 'w':
                    case 'e':
                    case 'r':
                    case 't':
                    case 'y':
                        return true;
                }
        return false;
    }

    /*
     Fukcja wyjscia
     */
    public boolean quit(String odpowiedzKlienta){
        if(odpowiedzKlienta.equals("z")) //z == wyjscie
            return true;
        return false;
    }
}