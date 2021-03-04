package com.apd.tema2.intersections;
import com.apd.tema2.entities.Intersection;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class SimpleStrictOne implements Intersection {
    // Define your variables here.

    // numarul de directii
    public Integer nr_dir;

    // timpul cat sta in sensul giratoriu
    public Integer t_wait;

    // lista de semafoare corespunzatoare fiecarei directii
    public ArrayList<Semaphore> sphList;


}
