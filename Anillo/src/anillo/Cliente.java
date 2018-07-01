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
    public int _numeroNodo;
    
    public void run(){
        try {
            //Se setea por donde se envia
            Gson parseGson = new Gson();
            BufferedReader input =
            new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            String answer = input.readLine();
            Transporte transporte = new Transporte();
            transporte = parseGson.fromJson( answer , Transporte.class);            
            _socket.close();
            procesamientoDePaquetes(transporte, _numeroNodo);
        }catch( Exception e){
            System.out.println();
        } 
    }

    public Cliente(Socket _socket, int _numeroNodo) {
        this._socket = _socket;
        this._numeroNodo = _numeroNodo;
    }
    
    public static void procesamientoDePaquetes( Transporte transporte, int numeroNodo){
        
        for (int i = 0; i < transporte._paquetes.size(); i++) {
            try {
                Paquete paquete = transporte._paquetes.get(i-1);
                //Descargo el paquete
                sleep(10000);
                //Si esto sucede es porque es para mi
                if( paquete._nodoDestino == numeroNodo){
                    transporte._paquetes.remove(i-1);
                }
            } catch (Exception e) {
            }
        }
    }
}
