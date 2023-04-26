package com.ocdsb.mapletracker.api;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileManager {
    public FileManager() {}

    public String readFile(Context context, String fileName) {
        String contents;
        try (FileInputStream fis = context.openFileInput(fileName)) {
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
                return null;
            } finally {
                contents = stringBuilder.toString();
            }
        } catch (IOException e) {
            System.out.println("IO exception while READING file");
            return null;
        }
        return contents;
    }

    public void saveFile(Context context, String fileName, String content) {
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("IO exception while WRITING file");
        }
    }
}
