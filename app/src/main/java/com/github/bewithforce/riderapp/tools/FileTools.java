package com.github.bewithforce.riderapp.tools;

import android.content.Context;
import android.util.Log;

import com.github.bewithforce.riderapp.post.requestBeans.Order;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import retrofit2.Response;

public class FileTools {
    public static void writeToFile(Response<List<Order>> response, Context context){
        try {
            FileOutputStream fos = context.openFileOutput("config.txt", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            List<Order> list = response.body();
            try {
                while (list.contains(null))
                    list.remove(null);
            } catch (Exception e){
                Log.e("writeToFileException", e.getLocalizedMessage());
            }
            out.writeObject(list);
            out.close();
            fos.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static List<Order> readFromFile(Context context){
        try {
            FileInputStream fis = context.openFileInput("config.txt");
            ObjectInputStream is = new ObjectInputStream(fis);
            List<Order> list = (List<Order>)is.readObject();
            is.close();
            fis.close();
            Log.e("list size", String.valueOf(list.size()));
            while(list.contains(null))
                list.remove(null);
            return list;
        }
        catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
            return null;
        }
    }
}
