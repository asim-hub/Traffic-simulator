package com.apd.tema2.intersections;
import com.apd.tema2.entities.Intersection;
import java.util.concurrent.Semaphore;

public class SimpleRoundabout implements Intersection {
    // Define your variables here.

    // numarul maxim de masini care pot intra in sensul giratoriu la un anumit moment
    public Integer nr;
    // timpul de astepate in giratoriu
    public Integer t_wait;
    // semafor initializat la nr
    public Semaphore Car_Sem;

}
