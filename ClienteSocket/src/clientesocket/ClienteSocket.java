/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientesocket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author pedro
 */
public class ClienteSocket {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        try {
            String serverAddress = JOptionPane.showInputDialog(
                    "Enter IP Address of a machine that is\n" +
                            "running the date service on port 9092:");
            Socket s = new Socket(serverAddress, 9092);
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(s.getInputStream()));
            String answer = input.readLine();
            JOptionPane.showMessageDialog(null, answer);
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(ClienteSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
