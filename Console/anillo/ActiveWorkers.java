/**
 * ActiveWorkers
 */
public class ActiveWorkers {
    private static ActiveWorkers instancia = null;
    private int count;

    private ActiveWorkers(){
        count = 0;
    }

    public static ActiveWorkers getInstance(){
        if (instancia == null) {
            instancia = new ActiveWorkers();
        }
        return instancia;
    }

    public int getCount(){
        return count;
    }

    public void addWorker(){
        count++;
    }

    public void removeWorker(){
        if (count > 0) {
            count--;
        }
    }
    
}