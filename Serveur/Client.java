package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import inc.Fonction;
import inputOutput.MyObjectInputStream;
import objets.Table;

import java.io.*;
import java.io.*;

/*
 * www.codeurjava.com
 */
public class Client {

    public static void main(String[] args) {
        final Socket clientSocket;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);// pour lire à partir du clavier

        String ipLocal = "localhost";
        int port = 1793;

        try {
            /*
             * les informations du serveur ( port et adresse IP ou nom d'hote
             * 127.0.0.1 est l'adresse local de la machine
             */
            System.out.println("Mode client");
            clientSocket = new Socket(ipLocal, port);
            System.out.println("server trouve : "+ipLocal);

            // flux pour envoyer
            out = new PrintWriter(clientSocket.getOutputStream());
            // flux pour recevoir
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread envoyer = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    while (true) {
                        msg = sc.nextLine();
                        out.println(msg);
                        out.flush();
                    }
                }
            });
            envoyer.start(); 

            Thread recevoir = new Thread(new Runnable() {
                //String msg;
                
                Fonction f = new Fonction();
                
                // if(relationObjIs == null) {
                //     relationObjIs = new ObjectInputStream(is);
                // }

                @Override
                public void run() {
                    try {
                        // msg = in.readLine();
                        // relationObjIs = new ObjectInputStream(is); //pour avoir l'objet envoyé 
                        InputStream is = clientSocket.getInputStream(); //lire le message envoyé

                        ObjectInputStream relationObjIs = new ObjectInputStream(is);

                        while (relationObjIs != null) {
                            try {
                                // System.out.println("yes");
                                Object relationObj  = relationObjIs.readObject(); //lecture de l'objet envoyé

                                // Object relationObj  = relationObjIs.readUnshared(); //lecture de l'objet envoyé

                                Table relation = (Table) relationObj;

                                f.displayAll(relation);

                            } catch (Exception e) {
                                System.err.println(e);
                            }
                            // System.out.println("Serveur : " + msg);
                            // msg = in.readLine();
                        }

                        System.out.println("Serveur deconnecté");
                        out.close();
                        relationObjIs.close();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            recevoir.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}