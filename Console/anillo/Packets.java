import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

/**
 * Clase con patron singleton para tener en todos lados los mismos paquetes, independientemente el hilo
 */
public class Packets {
    private static Packets instancia = null;
    public static ArrayList<Paquete> _paquetes =  new ArrayList<Paquete>();



    private Packets(){

    }

    public int tamano(){
        return _paquetes.size();
    }

    public static Packets getInstance(){
        // determina los destinos de cada paquete
        int cantidadPaquetes = ThreadLocalRandom.current().nextInt(0,4);

        if (instancia == null) {
            instancia = new Packets();
            for (int i = 0; i < cantidadPaquetes; i++) {
                _paquetes.add( new Paquete() );
            }
        }

        return instancia;

    }


    public void addPacket(){
       // count++;
    }

    public Paquete removePacket( int nodoDestino){
        Paquete paquete = new Paquete();
        boolean suiche = false;

        for (int i = 0; i < _paquetes.size(); i++) {
            //Al encontra el primero se rompe el ciclo
            if( _paquetes.get(i)._nodoDestino == nodoDestino){
                paquete._carga = _paquetes.get(i)._carga;
                paquete._nodoDestino = _paquetes.get(i)._nodoDestino;
                paquete._tiempo = _paquetes.get(i)._tiempo;
                _paquetes.remove(i);
                suiche = true;
                break;
            }
        }

        if (suiche == true) {
            System.out.println("Se elimino algun elemento");

        }else{
            System.out.println("No se elimino nada");
        }

        return paquete;
    }
    
}