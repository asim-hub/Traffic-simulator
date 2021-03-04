package com.apd.tema2;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.intersections.Railroad;
import com.apd.tema2.intersections.SimpleIntersection;
import com.apd.tema2.io.Reader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Main {
    public static Pedestrians pedestrians = null;
    public static Intersection intersection;
    public static int carsNo;
    // bariera ciclica pentru a ma asigura ca toate masinile
    // ajung la sensul giratoriu inainte sa treaca mai departe
    public static CyclicBarrier Reached;
    public static ArrayBlockingQueue a;
    public static ArrayBlockingQueue b;

    public static void main(String[] args) {
        Reader fileReader = Reader.getInstance(args[0]);
        Set<Thread> cars = fileReader.getCarsFromInput();
        a = new ArrayBlockingQueue<Integer>(carsNo);
        b = new ArrayBlockingQueue<Integer>(carsNo);

        //mecanism de sincronizare a mai multor threaduri pentru a bloca un nr specificat de threaduri
        Reached = new CyclicBarrier(cars.size());

        for(Thread car : cars) {
            car.start();
        }

        if(pedestrians != null) {
            try {
                Thread p = new Thread(pedestrians);
                p.start();
                p.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(Thread car : cars) {
            try {
                car.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
