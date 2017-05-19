package org.app.prevengapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ReporteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);
        final ListView listaCitas=(ListView)findViewById(R.id.lvListaReporte);
        List<String> reportes = new ArrayList<>();
        for (int x=0;x<20;x++){
            reportes.add(String.valueOf(x));
        }
        listaCitas.setAdapter(new ReporteAdapter(this, reportes));

    }
}
