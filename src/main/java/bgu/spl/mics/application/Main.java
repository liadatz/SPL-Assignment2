package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        List<Integer> a = new ArrayList<Integer>();
        a.add(0);
        a.add(1);
        a.add(2);
        a.remove(1);
        a.get(1);
        // Read from JSON file and import to java Object
        Gson gson = new Gson();
        Reader reader = new FileReader(args[0]);
        Input input = gson.fromJson(reader, Input.class);

        // Initiate PassiveObjects
        Ewoks ewoks = Ewoks.getInstance(input.getEwoks());

        // MicroServices Construction
        HanSoloMicroservice HanSolo = new HanSoloMicroservice();
        C3POMicroservice C3PO = new C3POMicroservice();
        LeiaMicroservice Leia = new LeiaMicroservice(input.getAttacks());
        R2D2Microservice R2D2 = new R2D2Microservice(input.getR2D2());
        LandoMicroservice Lando = new LandoMicroservice(input.getLando());

        // Threads
        Thread HanSoloThread = new Thread(HanSolo);
        Thread C3POThread = new Thread(C3PO);
        Thread LeiaThread = new Thread(Leia);
        Thread R2D2Thread = new Thread(R2D2);
        Thread LandoThread = new Thread(Lando);

        // Threads Start
        HanSoloThread.start();
        C3POThread.start();
        LeiaThread.start();
        R2D2Thread.start();
        LandoThread.start();

        // Threads Join
        HanSoloThread.join();
        C3POThread.join();
        R2D2Thread.join();
        LandoThread.join();
        LeiaThread.join();

        // Diary to JSON File

    }
}