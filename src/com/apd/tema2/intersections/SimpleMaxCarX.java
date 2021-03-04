package com.apd.tema2.intersections;
import com.apd.tema2.entities.Intersection;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class SimpleMaxCarX implements Intersection {
    // Define your variables here.

    //nr de directii
    public Integer nr_dir;
    //timp cat sta in giratoriu
    public Integer t_wait;
    //numarul maxim de masini care pot intra o data pe o directie
    public Integer x;
    //lista de semafoare corespunzatoare fiecarei directii in parte
    public ArrayList<Semaphore> sphList;

}
