/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.cajeroautomatico;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Germán
 */
public class CajeroAutomatico {
    
    Scanner reader;
    int cuenta;
    
    public CajeroAutomatico(){
        reader = new Scanner(System.in);
        System.out.println("Ingrese su número de cuenta:");
        cuenta = reader.nextInt();
    }

    private static void mostrarMenu() {
        System.out.println("Elija una opción");
        System.out.println("1. Extraer");
        System.out.println("2. Depositar");
        System.out.println("3. Transferir");
        System.out.println("4. Salir");
    }

    public static void main(String[] args) {
        Socket s;
        PrintWriter pw;
        CajeroAutomatico objCajero = new CajeroAutomatico();

        CajeroAutomatico.mostrarMenu();
        int entrada = objCajero.reader.nextInt();
        while (entrada != 4) {
            switch (entrada) {
                case 1:                    
                     {
                        try {
                            System.out.println("Ingrese el monto a extraer:");
                            float monto = objCajero.reader.nextFloat();
                            
                            s = new Socket(InetAddress.getByName("127.0.0.1"), 25000);
                            pw = new PrintWriter(s.getOutputStream(), true);
                            pw.println("1," +objCajero.cuenta + "," + monto);
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(CajeroAutomatico.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(CajeroAutomatico.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    //
                    //pw.println("Hola Servidor");
                    break;

                case 2:
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Opción inválida, ingrese nuevamente.");
            }

            CajeroAutomatico.mostrarMenu();
            entrada = objCajero.reader.nextInt();
        }

        System.exit(0);

    }
}
