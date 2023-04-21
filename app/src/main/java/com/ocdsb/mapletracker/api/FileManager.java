package com.ocdsb.mapletracker.api;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileManager {
    public Context context;
    public FileManager() {}

    public void saveFile() {
        File file = new File("maple.trees");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println(e);
                System.out.println("Could not create file");
                return;
            }
        }
        String content = "Smiley cat";
        try (FileOutputStream fos = context.openFileOutput("maple.trees", Context.MODE_PRIVATE)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("IO exception");
            return;
        }
        System.out.println("File created");
    }
}
