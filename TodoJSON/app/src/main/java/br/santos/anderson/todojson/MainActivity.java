package br.santos.anderson.todojson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import br.santos.anderson.todojson.infra.Conexao;
import br.santos.anderson.todojson.modelo.Item;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    private Conexao logar;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onClick(View v) throws IOException {
        String jsonFile = logar.conectar();

        //inicializacao da parte visual
        LinearLayout parentLayout = (LinearLayout)findViewById(R.id.linearlayout);
        CheckBox checkBox;

        //tratamento json
        final ObjectMapper mapper = new ObjectMapper();

        //referencia para o arquivo json
        //File internalStorageDir = getFilesDir();
        //File jsonFile = new File(internalStorageDir, "todo.json");

        //carregamento da array json para o array java
        Item[] lista = mapper.readValue(jsonFile, Item[].class);

        for(Item item: lista){
            checkBox = new CheckBox(MainActivity.this);
            checkBox.setText(item.getTitle());
            if(item.getCompleted().equalsIgnoreCase("true")) checkBox.setChecked(true);

            parentLayout.addView(checkBox);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        intent= new Intent(this, Conexao.class);
        bindService(intent, this , Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Conexao.MyBinder b = (Conexao.MyBinder) iBinder;
        logar = b.getService();

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        logar=null;
    }
}