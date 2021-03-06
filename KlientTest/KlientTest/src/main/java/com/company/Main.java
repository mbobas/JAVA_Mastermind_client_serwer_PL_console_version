package com.company;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
         Scanner sc = new Scanner(System.in);

        try {
            //ustanowienei gniazda
            Socket socket = new Socket("localhost", 8080);

            String odpTekst = "";
            while (!odpTekst.equals("z")) {
                //czytanie odpowiedzi z serwera
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                odpTekst = br.readLine();
                JSONObject odp = new JSONObject(odpTekst);
                System.out.println(odp.opt("response1"));


                //pobieranie danych z consoli
                String userInput = sc.next();
                String guess = userInput;

                //przypisanie odpowiedzi do obiektu JSON
                JSONObject json = new JSONObject();
                json.put("check", guess);
                //pisanie do Servera
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw.write(json.toString());
                bw.newLine();
                bw.flush();
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
