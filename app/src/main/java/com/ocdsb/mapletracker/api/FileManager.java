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
        // Read the contents of a filename (stored in internal Android storage).
        String contents;
        try (FileInputStream fis = context.openFileInput(fileName)) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            // Read chunk by chunk from the file.
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (IOException e) {
                return null;
            } finally {
                contents = stringBuilder.toString();
            }
        } catch (IOException e) {
            return null;
        }
        return contents;
    }

    public void saveFile(Context context, String fileName, String content) {
        // Save a file to the internal Android storage.
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("IO exception while WRITING file");
        }
    }
    public void deleteFile(Context context, String fileName){
        context.deleteFile(fileName);
        System.out.println("The file " + fileName + " was deleted!");
    }
}
