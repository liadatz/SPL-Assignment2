package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.CountDownLatch;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
    // CountDownLatch for Leia and Attackers
    public static CountDownLatch waitForAttackers = new CountDownLatch(4);

    public static void main(String[] args) throws IOException, InterruptedException {
        // Read from JSON file and import to java Object
        for (int i = 0; i < 50; i++) {
            Gson gson = new Gson();
        Reader reader = new FileReader("./Tests.json");
        Input input = gson.fromJson(reader, Input.class);

        // Initiate PassiveObjects
        Ewoks ewoks = Ewoks.getInstance(input.getEwoks());
        Diary diary = Diary.getInstance();

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
            FileWriter writer = new FileWriter(args[1]);
            gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(diary, writer);
            writer.flush();
            writer.close();
            System.out.println("******************************finish test number: "+i);
        }
    }
}