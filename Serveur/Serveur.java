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
import threads.Recepteur;

public class Serveur {

    public static void main(String[] test) {

        try {
            final ServerSocket serveurSocket;
            // final Socket clientSocket;
            // final BufferedReader in;
            // final PrintWriter out;
            // final Scanner sc = new Scanner(System.in);
            String ipWithDongle = "192.168.10.174";
            String ipSansDongle = "192.168.10.163";
	    String nouveauIP = "192.168.10.115";
            String local = "localhost";
            int port = 1793;

            serveurSocket = new ServerSocket(port);
            System.out.println("Mode serveur");
            System.out.println("En attente de client...");
            System.out.println("mon ip : " + local);
            System.out.println("port : " + port);

            while (true) {
                try {
                    Socket clientSocket = serveurSocket.accept();
                    System.out.println("nouveau client trouve");
                    Recepteur recepteur = new Recepteur(clientSocket); 
                    recepteur.start();
                } catch (Exception e) {
                System.err.println(e);
                }   
            } 
        } catch (Exception e) {
            System.err.println(e);
        }    
    }
}