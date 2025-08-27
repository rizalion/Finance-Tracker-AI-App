package com.example.financetracker.ml;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ExpensePredictor {
    private Interpreter tflite;

    public ExpensePredictor(Context context) {
        try {
            tflite = new Interpreter(loadModelFile(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd("expense_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public float predictExpense(int dayOfWeek, String category, float frequency, float pastExpense) {
        // Prepare input
        float[][] input = new float[1][4];
        input[0][0] = dayOfWeek;

        // Convert category to numerical value
        int categoryCode = 0;
        switch (category.toLowerCase()) {
            case "food":
                categoryCode = 0;
                break;
            case "transport":
                categoryCode = 1;
                break;
            case "entertainment":
                categoryCode = 2;
                break;
            case "bills":
                categoryCode = 3;
                break;
            case "shopping":
                categoryCode = 4;
                break;
        }

        input[0][1] = categoryCode;
        input[0][2] = frequency;
        input[0][3] = pastExpense;

        // Prepare output
        float[][] output = new float[1][1];

        // Run inference
        tflite.run(input, output);

        return output[0][0];
    }

    public void close() {
        tflite.close();
    }
}