package com.retroid.quiz.time.ultimate.trivia;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Aditya on 28-02-2015.
 */
public class AssetsCopy {

    static void copyFileFromAssets(Context context, String file, String dest) throws Exception {
        InputStream in = null;
        OutputStream fout = null;
        int count;

        try {
            in = context.getAssets().open(file);
            fout = new FileOutputStream(new File(dest));

            byte data[] = new byte[1024];
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //
                }
            }
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    //
                }
            }
        }
    }
}
