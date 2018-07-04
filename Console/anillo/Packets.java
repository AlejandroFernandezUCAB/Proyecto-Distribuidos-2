import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

/**
 * Packets
 */
public class Packets {
    private static Packets instancia = null;
    public static ArrayList<Paquete> _paquetes =  new ArrayList<Paquete>();

    private int count;

    private Packets(){
        count = 0;
    }

    public int tamano(){
        return _paquetes.size();
    }
    public static Packets getInstance(){
        int cantidadPaquetes = ThreadLocalRandom.current().nextInt(0,5);

        if (instancia == null) {
            instancia = new Packets();
            for (int i = 0; i < cantidadPaquetes; i++) {
                _paquetes.add( new Paquete() );
            }
        }

        return instancia;

    }

    public int getCount(){
        return count;
    }

    public void addPacket(){
        count++;
    }

    public void removePacket( int nodoDestino){
        boolean suiche = false;
        for (int i = 0; i < _paquetes.size(); i++) {
            if( _paquetes.get(i)._nodoDestino == nodoDestino){
                System.out.println( "El paquete tenia como destino:" + _paquetes.get(i)._nodoDestino);
                _paquetes.remove(i);
                suiche = true;
            }
        }

        if (suiche == true) {
            System.out.println("Se elimino algun elemento");
        }else{
            System.out.println("No se elimino nada");
        }
    }
    
}