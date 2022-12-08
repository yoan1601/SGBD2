package threads;

import inc.*;
import objets.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Recepteur extends Thread {

    Socket clientSocket;

    public Recepteur (Socket c) {
        clientSocket = c;
    }

    public void run () {

        try {

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
        
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            String requete = in.readLine();
            // tant que le client est connecté
            
            OutputStream os = clientSocket.getOutputStream(); // send message au client

            ObjectOutputStream mpSent = new ObjectOutputStream(os); // pour l'envoie de l'objet

            while (requete != null) {

                try {
                    Fonction f = new Fonction();
                    
                    System.out.println("Client : " + requete);

                    if(requete.equalsIgnoreCase("quit")) break;

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

                    mpSent.writeObject(e);
                }

                requete = in.readLine();
            }
            // sortir de la boucle si le client a déconecté
            System.out.println("Client deconnecté");
            // fermer le flux et la session socket
            out.close();

            clientSocket.close();

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}