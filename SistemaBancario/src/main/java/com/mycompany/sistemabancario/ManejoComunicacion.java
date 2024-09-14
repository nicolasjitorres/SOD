/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemabancario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Germán
 */
public class ManejoComunicacion extends Thread {

    Socket s;

    public ManejoComunicacion(Socket sc) {
        s = sc;
    }

    @Override
    public void run() {
        try {
            int opcion = 0;
            while (opcion != 4) {
                BufferedReader bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String cadena = bf.readLine();
                String[] entrada = cadena.split(",");
                switch(entrada[0]){
                    case "1": //Extracción
                        CuentaBancaria objCuenta = new CuentaBancaria();                        
                        objCuenta.Extraer(Integer.parseInt(entrada[1]), Float.parseFloat(entrada[2]));
                        break;
                    case "2": //Retiro
                        break;
                    case "3": //Transferencia
                        break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ManejoComunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
