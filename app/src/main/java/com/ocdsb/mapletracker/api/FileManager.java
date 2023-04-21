package com.ocdsb.mapletracker.api;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileManager {
    public Context context;
    public FileManager() {}

    public void readFile() {
        try (FileInputStream fis = context.openFileInput("maple.trees")) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (IOException e) {
                System.out.println("IO exception while READING file");
                System.out.println(e);
            } finally {
                String contents = stringBuilder.toString();
                System.out.println(contents);
            }
        } catch (IOException e) {
            System.out.println("IO exception while READING file");
            System.out.println(e);
        }
    }

    public void saveFile() {
        String content = "Smiley cat";
        try (FileOutputStream fos = context.openFileOutput("maple.trees", Context.MODE_PRIVATE)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("IO exception while WRITING file");
            System.out.println(e);
            return;
        }
        System.out.println("File created");
    }
}
