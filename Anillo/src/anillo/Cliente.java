/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anillo;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;


/**
 *
 * @author pedro
 */
public class Cliente extends Thread{
    
    public Socket _socket;
    
    public void run(){
        try {
            //Se setea por donde se envia
            Gson parseGson = new Gson();
            BufferedReader input =
            new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            String answer = input.readLine();
            Transporte hola = new Transporte();
            hola = parseGson.fromJson( answer , Transporte.class);
            System.out.println(answer);
            _socket.close();
        }catch( Exception e){
            System.out.println();
        } 
    }

    public Cliente(Socket _socket) {
        this._socket = _socket;
    }
    
    
}
