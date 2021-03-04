package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.ReaderHandler;
import com.apd.tema2.intersections.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Returneaza sub forma unor clase anonime implementari pentru metoda de citire din fisier.
 */
public class ReaderHandlerFactory {

    public static ReaderHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of them)
        // road in maintenance - 1 lane 2 ways, X cars at a time
        // road in maintenance - N lanes 2 ways, X cars at a time
        // railroad blockage for T seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) {
                    // preiau instanta aferenta
                    Main.intersection = IntersectionFactory.getIntersection("simpleIntersection");

                }
            };
            case "simple_n_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    // preiau instanta aferenta
                    Main.intersection = IntersectionFactory.getIntersection("simple_n_roundabout");

                    // citesc datele de pe ultima linie
                    String[] line = br.readLine().split(" ");

                    // le convertesc la Integer
                    ((SimpleRoundabout)Main.intersection).nr = Integer.parseInt(String.valueOf(line[0]));
                    ((SimpleRoundabout)Main.intersection).t_wait = Integer.parseInt(String.valueOf(line[1]));

                    // instantiez semaforul
                    ((SimpleRoundabout)Main.intersection).Car_Sem = new Semaphore(((SimpleRoundabout)Main.intersection).nr);
                }
            };
            case "simple_strict_1_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    // preiau instanta aferenta
                    Main.intersection = IntersectionFactory.getIntersection("simple_strict_1_car_roundabout");

                    //citesc ultima linie de date
                    String[] line = br.readLine().split(" ");

                    //conversie la Integer
                    ((SimpleStrictOne)Main.intersection).nr_dir = Integer.parseInt(String.valueOf(line[0]));
                    ((SimpleStrictOne)Main.intersection).t_wait = Integer.parseInt(String.valueOf(line[1]));

                    //aloc lista de semafoare
                    ((SimpleStrictOne)Main.intersection).sphList = new ArrayList();

                    //initializez semafoarele din lista
                    for(int i = 0; i < ((SimpleStrictOne)Main.intersection).nr_dir; i++) {
                        ((SimpleStrictOne)Main.intersection).sphList.add(new Semaphore(1));
                    }
                }
            };
            case "simple_strict_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {

                    // preiau instanta aferenta
                    Main.intersection = IntersectionFactory.getIntersection("simple_strict_x_car_roundabout");

                    //citesc ultima linie de date
                    String[] line = br.readLine().split(" ");

                    //conversie la Integer a datelor citite
                    ((SimpleIntersectionX)Main.intersection).nr_dir = Integer.parseInt(String.valueOf(line[0]));
                    ((SimpleIntersectionX)Main.intersection).t_wait = Integer.parseInt(String.valueOf(line[1]));
                    ((SimpleIntersectionX)Main.intersection).x = Integer.parseInt(String.valueOf(line[2]));

                    //aloc lista de semafoare
                    ((SimpleIntersectionX)Main.intersection).sphList = new ArrayList();

                    //instantiez bariera ciclica
                    ((SimpleIntersectionX)Main.intersection).sem_exit = new CyclicBarrier(((SimpleIntersectionX)Main.intersection).nr_dir * ((SimpleIntersectionX)Main.intersection).x );


                    //instantiez semafoare din lista
                    for(int i = 0; i < ((SimpleIntersectionX)Main.intersection).nr_dir; i++) {
                        ((SimpleIntersectionX)Main.intersection).sphList.add(new Semaphore(((SimpleIntersectionX)Main.intersection).x));
                    }
                }
            };
            case "simple_max_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    // preiau instanta aferenta
                    Main.intersection = IntersectionFactory.getIntersection("simple_max_x_car_roundabout");

                    //citesc ultima linie de date
                    String[] line = br.readLine().split(" ");

                    //conversie la Integer si aloc spatiu pentru semafoare
                    ((SimpleMaxCarX)Main.intersection).sphList = new ArrayList();
                    ((SimpleMaxCarX)Main.intersection).nr_dir = Integer.parseInt(String.valueOf(line[0]));
                    ((SimpleMaxCarX)Main.intersection).t_wait = Integer.parseInt(String.valueOf(line[1]));
                    ((SimpleMaxCarX)Main.intersection).x = Integer.parseInt(String.valueOf(line[2]));

                    //instantiez semafoare
                    for(int i = 0; i < ((SimpleMaxCarX)Main.intersection).nr_dir; i++) {
                        ((SimpleMaxCarX)Main.intersection).sphList.add(new Semaphore(((SimpleMaxCarX)Main.intersection).x));
                    }
                }
            };
            case "priority_intersection" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    
                }
            };
            case "crosswalk" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    
                }
            };
            case "simple_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    
                }
            };
            case "complex_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    
                }
            };
            case "railroad" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    // preiau instanta aferenta
                    Main.intersection = IntersectionFactory.getIntersection("railroad");

                }
            };
            default -> null;
        };
    }

}
