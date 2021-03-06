package com.company;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Watek extends Thread {

    public Socket socket;

    private int dlugoscKodu;
    private String PASSCODE;
    private int iloscProb = 0;
    private Scanner sc = new Scanner(System.in);

    private boolean klientWygral;
    private boolean klientWyszedl;

    private SekwencjaPassCode passCodeCreate = new SekwencjaPassCode();
    private SprawdzanieSekwencji sprawdzaniesek;
    private SprawdzenieWejscia sprawdzaneieWejscia = new SprawdzenieWejscia();
    private JSONObject odp = new JSONObject();


    public Watek(Socket socket) throws IOException {
        this.socket = socket;
        // wypisanie powitania i poziomu trudnoscim
        //wygenerowanie kodu do odgadniecia po wybraniu poziomu
        //inicjalizacja chekkera
        printStart();
        PASSCODE = String.valueOf(passCodeCreate.losowanieSekwencji(dlugoscKodu));
        sprawdzaniesek = new SprawdzanieSekwencji(PASSCODE);
    }

    @Override
    public void run() {
        try {

            //pętla dopóki klient nei wygra, alebo skonczy mu sie 10 prob,
            while((iloscProb < 20) && !klientWygral){
                //czytanie odpowiedzi klienta
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String tekst = br.readLine();
                JSONObject json = new JSONObject(tekst);
                String guess = (String) json.opt("check");
                //napisanie odpowiedzi do klienta
                String guessInput = zapytajOOdpowiedz(guess);
                if(klientWyszedl) {
                    //napisanie odpowiedzi do klienta
                    napisanieOdpowiedziDoKlienta("Wyszedłeś z gry, było blisko lub daleko, prawdiłowa odpowiedź to:  " + PASSCODE);

                    break;
                }

                runGuess(guessInput);

                iloscProb++;
            }
            koniecGry();






        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * wydruk na wyjscie powitania i zasad
     * wybór poziomu trudnosci wyborPoziomuTrudnosci()
     */
    public void printStart() throws IOException {
        napisanieOdpowiedziDoKlienta("\n******************************************************\n" +
                "///\t\t\tWITAJ W MASTERMIND\t\t\t///\n" +
                "******************************************************\n"+
                "Wybierz poziom Easy(e), Normal(n), Hard(h), Master(m)"
        );
        dlugoscKodu = wyborPoziomuTrudnosci();
        napisanieOdpowiedziDoKlienta("Zgadnij wszystkie " + dlugoscKodu + "-znaki mastremainda w 20tu próbach\n" +
        "Poprawne znaki to:  'q', 'w', 'e', 'r', 't', and 'y'\n" +
        "Wpisz 'z' aby wyjcie.\n"+
        "****************************************\n" +
        "******************** b - litera jest ok, ale na niewlasciwej pozycji **********************\n"
        + "******************** B - litera i pozycja litery jest zgodna **********************\n" +
                "******************** BBB - oznacza wygraną **********************\n" +
        "Podaj " + dlugoscKodu + " znaki, bez sapcji np. \"qwe\", wer itp. " );
    }

    /*
     * Prompt user to choose difficulty, which determines length of passcode
     * Recursion occurs until input is valid.
     * @return int equal to length of passcode.
     */
    public int wyborPoziomuTrudnosci() throws IOException {
        int inputStatus = sprawdzaneieWejscia.poprawnyPoziomSprawdzenie(czytanieOdpowiedziOdKlienta());
        if(inputStatus != 0) //inputStatus is valid (either e, n, h, m)
            return inputStatus;
        napisanieOdpowiedziDoKlienta("Blędny wybór");
        return wyborPoziomuTrudnosci(); // rekurencja dopki nie poda wlasciwej odpowiedzi
    }

    /*
     * Print user input instruction, scan user input, call checkValidInput.
     * Recursion occurs until input is valid.
     * @return String user guess for passcode
     */

    public String zapytajOOdpowiedz(String userInput) throws IOException {

        System.out.println("SERVER 114: " + userInput);
        if(sprawdzaneieWejscia.poprawnaOdpowiedzSprawdzenie(userInput, PASSCODE.length())){
            if(sprawdzaneieWejscia.quit(userInput)) //check if user quits
                klientWyszedl = true;
            return userInput;
        }
        napisanieOdpowiedziDoKlienta("Niewlasciwe wprowadzenie! Spójrz na tekst jescze raz");
        //czytanie odpowiedzi klienta
//        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        String tekst = br.readLine();
//        JSONObject json = new JSONObject(tekst);
//        String userInput2 = (String) json.opt("check");
        String userInput2 = czytanieOdpowiedziOdKlienta();
        System.out.println("Odpowiedź klienta 131: " + userInput);

        return zapytajOOdpowiedz(userInput2);  //recurse until input is valid
    }

    /*
     * Sprawdzenie odpowiedzi usera
     */
    public void runGuess(String guess) throws IOException {
        String odpowiedz = String.valueOf(sprawdzaniesek.sprawdzOdpowiedz(PASSCODE, guess));
        odpowiedz += "\nTo była Próba #" + iloscProb + ":\nZgaduj! ";
        //napisanie odpowiedzi do klienta
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        odp.put("response1", odpowiedz); //
        bw.write(odp.toString());
        bw.newLine();
        bw.flush();
        if(sprawdzaniesek.sprawdzCzyWygrana()) //check if the user won.
            klientWygral = true;

    }

    /*
     * komunikaty na koniec gry
     */
    public void koniecGry() throws IOException {
        if(klientWyszedl)
        {
            napisanieOdpowiedziDoKlienta("Wyszedłeś z gry, było blisko lub daleko, prawdiłowa odpowiedź to:  " + PASSCODE);

        }
        else if(klientWygral){
            //czytanieOdpowiedziOdKlienta();
            napisanieOdpowiedziDoKlienta("Gratulacje, Wygrałeś! " + iloscProb);
            //System.exit(0);
        }

        else {

        }

        napisanieOdpowiedziDoKlienta("Zbyt wiele prób, PRZEGRAłEś, poprawny kod to: " + PASSCODE);
        System.out.println("\n****************************************");
        //System.exit(0);
    }

    String czytanieOdpowiedziOdKlienta() throws IOException {
        //czytanie odpowiedzi klienta
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String tekst = br.readLine();
        JSONObject json = new JSONObject(tekst);
        String guess = (String) json.opt("check");
        System.out.println("Odpowiedź klienta 165: " + guess);
        return guess;
    }
    public void napisanieOdpowiedziDoKlienta(String odpowiedz) throws IOException {
        //napisanie odpowiedzi do klienta
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        odp.put("response1", odpowiedz); //wyjscie, zamknij, quit
        bw.write(odp.toString());
        bw.newLine();
        bw.flush();
    }


}
