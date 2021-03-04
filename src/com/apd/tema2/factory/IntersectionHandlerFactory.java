package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.*;
import com.apd.tema2.intersections.*;
import com.apd.tema2.utils.Constants;
import java.lang.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;
import java.awt.*;

import static java.lang.Thread.sleep;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {
    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // mesaj ca masina a ajuns la semafor
                    System.out.println("Car " + car.getId() + " has reached the semaphore, now waiting...");

                    // cat timp asteapta la semafor
                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // masina porneste la drum
                    System.out.println("Car " + car.getId() + " has waited enough, now driving...");
                }
            };
            case "simple_n_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {

                    // mesaj ca masina a ajuns la semafor
                    System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");

                    // se încearcă trecerea de semafor; dacă numărul de thread-uri aflate în zona critică este
                    // mai mic decât numărul maxim de thread-uri acceptate, thread-ul poate intra
                    try {
                        ((SimpleRoundabout)Main.intersection).Car_Sem.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // afisez ca am intrat in giratoriu
                    System.out.println("Car " + car.getId() + " has entered the roundabout");

                    // timpul cat stau in giratoriu
                    try {
                        sleep(((SimpleRoundabout)Main.intersection).t_wait);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // mesaj cand ies din sensul giratoriu
                    System.out.println("Car " + car.getId() + " has exited the roundabout after " + (((SimpleRoundabout)Main.intersection).t_wait / 1000) + " seconds");

                    // apeleză metoda pentru a anunța faptul că am terminat treaba în zona critică,
                    // permițând astfel incrementarea numărului de thread-uri car pot intra în zona critică
                    ((SimpleRoundabout)Main.intersection).Car_Sem.release();

                }
            };
            case "simple_strict_1_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // mesaj ca masina a ajuns la semafor
                    System.out.println("Car " + car.getId() + " has reached the roundabout");

                    // se încearcă trecerea de semafor-ul corespunzator directiei actuale
                    try {
                        ((SimpleStrictOne)Main.intersection).sphList.get(car.getStartDirection()).acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // mesaj ca am intrat in intersectie de pe directia actuala
                    System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());

                    // sleep cat sta in intersectie
                    try {
                        sleep(((SimpleStrictOne)Main.intersection).t_wait);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // mesaj ca am iesit din intersectie
                    System.out.println("Car " + car.getId() + " has exited the roundabout after " + (((SimpleStrictOne)Main.intersection).t_wait / 1000) + " seconds");

                    // apeleză metoda pentru a anunța faptul că am terminat treaba în zona critică,
                    // corespunzatoare directiei actuale
                    ((SimpleStrictOne)Main.intersection).sphList.get(car.getStartDirection()).release();

                }
            };
            case "simple_strict_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {

                    // mesaj ca masina a ajuns la semafor
                    System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");

                    // bariera ciclica - ma asigur ca toate masinile sa ajunga la sensul giratoriu inainte sa se treaca mai departe
                    // pentru asta foloesesc o bariera ciclica pe care o instantiez la numarul total de masini
                    try{
                        Main.Reached.await();
                    } catch (BrokenBarrierException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    // semafor-directie pentru a ma asigura ca sunt permise maxim x masini pe o directie
                    try {
                        ((SimpleIntersectionX)Main.intersection).sphList.get(car.getStartDirection()).acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // timpul cat astept in intersectie
                    try{
                        ((SimpleIntersectionX)Main.intersection).sem_exit.await();
                    } catch (BrokenBarrierException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    // printez ca a fost selectata masina
                    System.out.println("Car " + car.getId() + " was selected to enter the roundabout from lane " + car.getStartDirection());

                    // bariera ciclica pentru a ma asigura ca toate masinile parasesc sensul giratoriu inainte
                    // ca o runda noua de masini sa porneasca
                    try{
                        ((SimpleIntersectionX)Main.intersection).sem_exit.await();
                    } catch (BrokenBarrierException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    // print ca am intrat in intersectie de pe directia x
                    System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());

                    // sleep cat sta in intersectie
                    try {
                        sleep(((SimpleIntersectionX)Main.intersection).t_wait);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // mesaj ca am iesit din intersectie
                    System.out.println("Car " + car.getId() + " has exited the roundabout after " + (((SimpleIntersectionX)Main.intersection).t_wait / 1000) + " seconds");

                    // eliberez semaforul corespunzator directiei actuale
                    ((SimpleIntersectionX)Main.intersection).sphList.get(car.getStartDirection()).release();

                }
            };
            case "simple_max_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    // Continuati de aici

                    // mesaj ca masina a ajuns la semafor
                    System.out.println("Car " + car.getId() + " has reached the roundabout from lane " + car.getStartDirection());

                    // se încearcă trecerea de semafor pe directia masinii;
                    // dacă numărul de thread-uri aflate în zona critică (pe directie) este mai mic sau egal
                    // decât numărul maxim de thread-uri acceptate, thread-ul poate intra (masina intra in intersectie
                    // pe directia ei)
                    try {
                        ((SimpleMaxCarX)Main.intersection).sphList.get(car.getStartDirection()).acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // print ca am intrat in intersectie de pe directia masinii actuale
                    System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());

                    // timp cat sta in intersectie
                    try {
                        sleep(((SimpleMaxCarX)Main.intersection).t_wait);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // mesaj ca am iesit din intersectie
                    System.out.println("Car " + car.getId() + " has exited the roundabout after " + (((SimpleMaxCarX)Main.intersection).t_wait / 1000) + " seconds");

                    //eliberez semaforul directiei
                    ((SimpleMaxCarX)Main.intersection).sphList.get(car.getStartDirection()).release();

                }
            };
            case "priority_intersection" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    // Continuati de aici
                }
            };
            case "crosswalk" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    
                }
            };
            case "simple_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    
                }
            };
            case "complex_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    
                }
            };
            case "railroad" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // mesaj ca masina a ajuns la semafor
                    System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has stopped by the railroad");

                    //retin ordinea masinilor, dar si directia fiecareia
                    Main.a.add(car.getId());
                    Main.b.add(car.getStartDirection());

                    // bariera ciclica - ma asigur ca toate masinile sa ajunga la calea ferata
                    // inainte sa se treaca mai departe
                    // pentru asta foloesesc o bariera ciclica pe care o instantiez
                    // la numarul total de masini
                    try{
                        Main.Reached.await();
                    } catch (BrokenBarrierException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    //masinile incep sa se miste in ordinea in care au ajuns pe fiecare sens
                    if(car.getId() == Main.carsNo - 1) {
                        System.out.println("The train has passed, cars can now proceed");
                        while( Main.a.size() != 0) {
                            try {
                                System.out.println("Car " + Main.a.take() + " from side number " + Main.b.take() + " has started driving");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }




                }
            };
            default -> null;
        };
    }
}
