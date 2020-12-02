package br.santos.anderson.todojson.infra;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.StrictMode;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Conexao extends Service {
    public Conexao() {
    }

    private final IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        public Conexao getService() {
            return Conexao.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public String conectar() {
        HttpURLConnection conn = null;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            String url = "https://jsonplaceholder.typicode.com/todos";
            System.out.println(url);
            URL _url = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) _url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                System.out.println("Realizou a leitura");

                StringBuilder textBuilder = new StringBuilder();
                try (Reader reader = new BufferedReader(new InputStreamReader
                        (in, Charset.forName(StandardCharsets.UTF_8.name())))) {
                    int c = 0;
                    while ((c = reader.read()) != -1) {
                        textBuilder.append((char) c);
                    }
                }
                return textBuilder.toString();

            } finally {
                urlConnection.disconnect();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}