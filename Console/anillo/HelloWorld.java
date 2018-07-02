/**
 * HelloWorld
 */

public class HelloWorld {

    public static void main(String[] args){
        ActiveWorkers instancia = ActiveWorkers.getInstance();
        instancia.addWorker();
        instancia.addWorker();
        System.out.println(instancia.getCount());
        instancia.removeWorker();
        System.out.println(instancia.getCount());
        ActiveWorkers instancia2 = ActiveWorkers.getInstance();
        instancia2.addWorker();
        instancia2.addWorker();
        System.out.println(instancia2.getCount());
        instancia2.removeWorker();
        System.out.println(instancia2.getCount());
    }
}