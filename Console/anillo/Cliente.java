import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


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
        ActiveWorkers instancia = ActiveWorkers.getInstance();
        
        System.out.println("Recibir ---> Recibido Transporte id: " + transporte._id);
        for (int i = 0; i < transporte._paquetes.size(); i++) {
            try {

                Paquete paquete = transporte._paquetes.get(i);
                Boolean siHayTrabajadores = false;

                //Si esto sucede es porque es para mi y lo saco de donde esta
                if( paquete._nodoDestino == numeroNodo ){
                   
                    //Se queda pegado esperando que alguien se libere
                    sleep(1000);
                    while ( siHayTrabajadores == false ) {

                        if (instancia.getCount() < 2 ){
                            //Ejecutando el hilo del cliente
                            Escritorio _escritorioHilo = new Escritorio( transporte._paquetes.get(i));
                            instancia.addWorker();
                            _escritorioHilo.start();
                            System.out.println("Hay" + instancia.getCount() + " Trabajadores activos");
                            System.out.println("Procesar ---> Recibi un Paquete! lo quito del Transporte (id:" +transporte._id+")");
                            transporte._paquetes.remove(i);
                            siHayTrabajadores = true;

                        }else{

                            System.out.println("Todos los trabajdores andan ocupados, espere un momento");

                        }

                    }

                    
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
                    Socket socket = new Socket(this.nextNodeAddress, 9002);
                    PrintWriter out =
                            new PrintWriter(socket.getOutputStream(), true);
                        //Se manda a traves del socket
                        out.println( gsonAEnviar );


            } catch (IOException | InterruptedException ex) {
        }
    }
}
