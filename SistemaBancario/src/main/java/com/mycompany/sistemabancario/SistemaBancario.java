/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemabancario;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Germán
 */
public class SistemaBancario {

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(25000);
            while(true){
                System.out.println("Esperando conexiones");
                Socket s = ss.accept();
                ManejoComunicacion tC = new ManejoComunicacion(s);
                tC.start();
            }                        
        } catch (IOException ex) {
            Logger.getLogger(SistemaBancario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
