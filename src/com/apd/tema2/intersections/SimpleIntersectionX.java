package com.apd.tema2.intersections;
import com.apd.tema2.entities.Intersection;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;


public class SimpleIntersectionX implements Intersection {
    // Define your variables here.

    // numarul de directii
    public Integer nr_dir;
    // numarul maxim de masini care pot intra in intersectie pe o dir.
    public Integer x;
    // timpul cat sta in intersectie
    public Integer t_wait;
    //lista semaphoare directii
    public ArrayList<Semaphore> sphList;
    //bariera ciclica
    public CyclicBarrier sem_exit;
}