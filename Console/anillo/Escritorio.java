public class Escritorio extends Thread{
    
    public Paquete _paquete;
    
    public Escritorio( Paquete _paquete){

        this._paquete = _paquete;

    }

    public void run(){
        try {
            sleep(10000);
            //Procedo a guardar en el archivo el paquete
            Archivo archivo = new Archivo( String.valueOf(_paquete._carga) + " " +String.valueOf(_paquete._tiempo) );
            archivo.crearArchivo();
            //Matando el hilos
            interrupt();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}