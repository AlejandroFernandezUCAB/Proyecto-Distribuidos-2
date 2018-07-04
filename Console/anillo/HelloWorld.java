/**
 * HelloWorld
 */

public class HelloWorld {

    public static void main(String[] args){
        Packets instancia = Packets.getInstance();
        System.out.println("Hay:" + instancia.tamano() + " Paquetes");

        instancia.removePacket( 0 );
        
        System.out.println("Hay:" + instancia.tamano() + " Paquetes");
    }
}