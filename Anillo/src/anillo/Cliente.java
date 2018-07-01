/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anillo;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 *
 * @author pedro
 */
public class Cliente extends Thread{
    
    public Socket _socket;
    public int _numeroNodo;
    String nextNodeAddress;
    
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
            transporte = procesamientoDePaquetes(transporte, _numeroNodo);
            enviarToken(transporte);
            
        }catch( Exception e){
            System.out.println();
        } 
    }

    public Cliente(Socket _socket, int _numeroNodo, String nextNode) {
        this._socket = _socket;
        this._numeroNodo = _numeroNodo;
        this.nextNodeAddress = nextNode;
    }
    
    
    public static Transporte procesamientoDePaquetes( Transporte transporte, int numeroNodo){
        System.out.println("Recibir ---> Recibido Transporte id: " + transporte._id);
        for (int i = 0; i < transporte._paquetes.size(); i++) {
            try {
                Paquete paquete = transporte._paquetes.get(i);
                //Descargo el paquete
                sleep(10000);
                //Si esto sucede es porque es para mi y lo saco de donde esta
                if( paquete._nodoDestino == numeroNodo){
                    System.out.println("Procesar ---> Recibi un Paquete! lo quito del Transporte (id:" +transporte._id+")");
                    transporte._paquetes.remove(i);
                    
                }else{
                    
                    transporte._paquetes.get(i)._tiempo += 10;  
                    
                }
                
            } catch (Exception e) {
            }
        }
        return transporte;
    }
    public void enviarToken( Transporte transporte ){
        try {
                if(transporte._paquetes.size() == 0){
                    System.out.println("Enviar ---> Envio Transporte (id:" +transporte._id+") VACIO al siguiente nodo");
                    //return;
                }
                else{
                    System.out.println("Enviar ---> Envio Transporte (id:" +transporte._id+") al siguiente nodo");
                }
                // envialo al siguiente nodo
                
                    Thread.sleep(5000);
                    // serializamos
                    Gson gson = new Gson();
                    String gsonAEnviar = gson.toJson( transporte, Transporte.class);
                    // enviamos por el socket del servidor
                    // (el siguiente nodo)
                    Socket socket = new Socket(this.nextNodeAddress, 9003);
                    PrintWriter out =
                            new PrintWriter(socket.getOutputStream(), true);
                        //Se manda a traves del socket
                        out.println( gsonAEnviar );


            } catch (IOException | InterruptedException ex) {
        }
    }
}
