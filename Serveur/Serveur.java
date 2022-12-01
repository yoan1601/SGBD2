package socket;

import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.net.*;

import inc.Fonction;
import objets.Table;

/*
 * www.codeurjava.com
 */
public class Serveur {

    public static void main(String[] test) {

        final ServerSocket serveurSocket;
        final Socket clientSocket;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);
        String ipWithDongle = "192.168.10.174";
        String local = "localhost";
        int port = 1793;

        try {
            serveurSocket = new ServerSocket(port);
            System.out.println("Mode serveur");
            System.out.println("En attente de client...");
            System.out.println("mon ip : " + local);
            System.out.println("port : " + port);
            clientSocket = serveurSocket.accept();
            System.out.println("Client trouve");
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Thread envoi = new Thread(new Runnable() {
            // String msg;

            // @Override
            // public void run() {
            // while (true) {
            // msg = sc.nextLine();
            // out.println(msg);
            // out.flush();
            // }
            // }
            // });
            // envoi.start();

            Fonction f = new Fonction();

            Thread recevoir = new Thread(new Runnable() {
                String requete;

                @Override
                public void run() {
                    try {
                        requete = in.readLine();
                        // tant que le client est connecté
                                
                        OutputStream os = clientSocket.getOutputStream(); // send message au client

                        ObjectOutputStream mpSent = new ObjectOutputStream(os); // pour l'envoie de l'objet

                        while (requete != null) {

                            try {
                                System.out.println("Client : " + requete);

                                Table relation = f.traiterRequete(requete);

                                // // reponse : relation SGBD
                                // OutputStream os = clientSocket.getOutputStream(); // send message au client

                                Object obj = relation;
                                // objet a envoyé

                                mpSent.writeObject(obj); // envoie de l'obj

                                System.out.println("Server : relation sent");
                                // f.displayAll(relation);
                                f.display(relation);

                            } catch (Exception e) {
                                System.err.println(e);
                            }

                            requete = in.readLine();
                        }
                        // sortir de la boucle si le client a déconecté
                        System.out.println("Client deconnecté");
                        // fermer le flux et la session socket
                        out.close();
                        clientSocket.close();
                        serveurSocket.close();
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