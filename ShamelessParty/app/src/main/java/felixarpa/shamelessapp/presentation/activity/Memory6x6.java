package felixarpa.shamelessapp.presentation.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import felixarpa.shamelessapp.R;
import felixarpa.shamelessapp.domain.controller.exception.NoSuchPartyGoingOnException;
import felixarpa.shamelessapp.domain.data.PartyControllerImpl;
import felixarpa.shamelessapp.presentation.view.CoolImageFlipper;


/*

This code is from Proyecto Jedi: https://github.com/felixarpa/ProyectoJedi
by felixarpa
It was my first Android App.

 */


public class Memory6x6 extends AppCompatActivity {

    int[] combinations = new int[36];
    ImageView[] imageViews = new ImageView[36];
    Drawable[] drawables = new Drawable[18];
    boolean[] cartaGirada = new boolean[36];
    CoolImageFlipper coolImageFlipper;
    ProgressBar progressBar;
    TextView intentosTV;
    int intentosInt = 0;
    int primeraCarta = -1;
    int segundaCarta = -1;
    boolean unaGirada = false;
    //String perfil = "cat";
    View.OnClickListener list, nulo;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory6x6_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Memory");

        setViewsMemory(1);
        setCardsMemory();
        setViewsMemory(2);
        setListenerMemory();
        setViewsMemory(3);
    }

    private void setListenerMemory() {
        list = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.imageView00:
                        superFuncion(0);
                        break;

                    case R.id.imageView01:
                        superFuncion(1);
                        break;

                    case R.id.imageView02:
                        superFuncion(2);
                        break;

                    case R.id.imageView03:
                        superFuncion(3);
                        break;

                    case R.id.imageView04:
                        superFuncion(4);
                        break;

                    case R.id.imageView05:
                        superFuncion(5);
                        break;

                    case R.id.imageView06:
                        superFuncion(6);
                        break;

                    case R.id.imageView07:
                        superFuncion(7);
                        break;

                    case R.id.imageView08:
                        superFuncion(8);
                        break;

                    case R.id.imageView09:
                        superFuncion(9);
                        break;

                    case R.id.imageView10:
                        superFuncion(10);
                        break;

                    case R.id.imageView11:
                        superFuncion(11);
                        break;

                    case R.id.imageView12:
                        superFuncion(12);
                        break;

                    case R.id.imageView13:
                        superFuncion(13);
                        break;

                    case R.id.imageView14:
                        superFuncion(14);
                        break;

                    case R.id.imageView15:
                        superFuncion(15);
                        break;

                    case R.id.imageView16:
                        superFuncion(16);
                        break;

                    case R.id.imageView17:
                        superFuncion(17);
                        break;

                    case R.id.imageView18:
                        superFuncion(18);
                        break;

                    case R.id.imageView19:
                        superFuncion(19);
                        break;

                    case R.id.imageView20:
                        superFuncion(20);
                        break;

                    case R.id.imageView21:
                        superFuncion(21);
                        break;

                    case R.id.imageView22:
                        superFuncion(22);
                        break;

                    case R.id.imageView23:
                        superFuncion(23);
                        break;

                    case R.id.imageView24:
                        superFuncion(24);
                        break;

                    case R.id.imageView25:
                        superFuncion(25);
                        break;

                    case R.id.imageView26:
                        superFuncion(26);
                        break;

                    case R.id.imageView27:
                        superFuncion(27);
                        break;

                    case R.id.imageView28:
                        superFuncion(28);
                        break;

                    case R.id.imageView29:
                        superFuncion(29);
                        break;

                    case R.id.imageView30:
                        superFuncion(30);
                        break;

                    case R.id.imageView31:
                        superFuncion(31);
                        break;

                    case R.id.imageView32:
                        superFuncion(32);
                        break;

                    case R.id.imageView33:
                        superFuncion(33);
                        break;

                    case R.id.imageView34:
                        superFuncion(34);
                        break;

                    case R.id.imageView35:
                        superFuncion(35);
                        break;

                }

                intentosTV.setText(Integer.toString(intentosInt));
                if (progressBar.getProgress() == progressBar.getMax()) gameWin();
            }
        };
    }

    private void setViewsMemory(int i) {
        switch (i) {
            case 1:
                coolImageFlipper = new CoolImageFlipper(getApplicationContext());
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setMax(18);
                progressBar.setProgress(intentosInt);
                intentosTV = (TextView) findViewById(R.id.intentos);
                setTitle("Memory 6x6");

                imageViews[0]  = (ImageView) findViewById(R.id.imageView00);
                imageViews[1]  = (ImageView) findViewById(R.id.imageView01);
                imageViews[2]  = (ImageView) findViewById(R.id.imageView02);
                imageViews[3]  = (ImageView) findViewById(R.id.imageView03);
                imageViews[4]  = (ImageView) findViewById(R.id.imageView04);
                imageViews[5]  = (ImageView) findViewById(R.id.imageView05);
                imageViews[6]  = (ImageView) findViewById(R.id.imageView06);
                imageViews[7]  = (ImageView) findViewById(R.id.imageView07);
                imageViews[8]  = (ImageView) findViewById(R.id.imageView08);
                imageViews[9]  = (ImageView) findViewById(R.id.imageView09);
                imageViews[10] = (ImageView) findViewById(R.id.imageView10);
                imageViews[11] = (ImageView) findViewById(R.id.imageView11);
                imageViews[12] = (ImageView) findViewById(R.id.imageView12);
                imageViews[13] = (ImageView) findViewById(R.id.imageView13);
                imageViews[14] = (ImageView) findViewById(R.id.imageView14);
                imageViews[15] = (ImageView) findViewById(R.id.imageView15);
                imageViews[16] = (ImageView) findViewById(R.id.imageView16);
                imageViews[17] = (ImageView) findViewById(R.id.imageView17);
                imageViews[18] = (ImageView) findViewById(R.id.imageView18);
                imageViews[19] = (ImageView) findViewById(R.id.imageView19);
                imageViews[20] = (ImageView) findViewById(R.id.imageView20);
                imageViews[21] = (ImageView) findViewById(R.id.imageView21);
                imageViews[22] = (ImageView) findViewById(R.id.imageView22);
                imageViews[23] = (ImageView) findViewById(R.id.imageView23);
                imageViews[24] = (ImageView) findViewById(R.id.imageView24);
                imageViews[25] = (ImageView) findViewById(R.id.imageView25);
                imageViews[26] = (ImageView) findViewById(R.id.imageView26);
                imageViews[27] = (ImageView) findViewById(R.id.imageView27);
                imageViews[28] = (ImageView) findViewById(R.id.imageView28);
                imageViews[29] = (ImageView) findViewById(R.id.imageView29);
                imageViews[30] = (ImageView) findViewById(R.id.imageView30);
                imageViews[31] = (ImageView) findViewById(R.id.imageView31);
                imageViews[32] = (ImageView) findViewById(R.id.imageView32);
                imageViews[33] = (ImageView) findViewById(R.id.imageView33);
                imageViews[34] = (ImageView) findViewById(R.id.imageView34);
                imageViews[35] = (ImageView) findViewById(R.id.imageView35);
                break;

            case 2:
                intentosTV.setText(Integer.toString(intentosInt));

                drawables[0] = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat00);
                drawables[1] = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat01);
                drawables[2] = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat02);
                drawables[3] = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat03);
                drawables[4] = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat04);
                drawables[5] = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat05);
                drawables[6] = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat06);
                drawables[7] = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat07);
                drawables[8] = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat08);
                drawables[9] = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat09);
                drawables[10]= ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat10);
                drawables[11]= ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat11);
                drawables[12]= ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat12);
                drawables[13]= ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat13);
                drawables[14]= ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat14);
                drawables[15]= ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat15);
                drawables[16]= ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat16);
                drawables[17]= ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat17);
                break;

            case 3:
                imageViews[0] .setOnClickListener(list);
                imageViews[1] .setOnClickListener(list);
                imageViews[2] .setOnClickListener(list);
                imageViews[3] .setOnClickListener(list);
                imageViews[4] .setOnClickListener(list);
                imageViews[5] .setOnClickListener(list);
                imageViews[6] .setOnClickListener(list);
                imageViews[7] .setOnClickListener(list);
                imageViews[8] .setOnClickListener(list);
                imageViews[9] .setOnClickListener(list);
                imageViews[10].setOnClickListener(list);
                imageViews[11].setOnClickListener(list);
                imageViews[12].setOnClickListener(list);
                imageViews[13].setOnClickListener(list);
                imageViews[14].setOnClickListener(list);
                imageViews[15].setOnClickListener(list);
                imageViews[16].setOnClickListener(list);
                imageViews[17].setOnClickListener(list);
                imageViews[18].setOnClickListener(list);
                imageViews[19].setOnClickListener(list);
                imageViews[20].setOnClickListener(list);
                imageViews[21].setOnClickListener(list);
                imageViews[22].setOnClickListener(list);
                imageViews[23].setOnClickListener(list);
                imageViews[24].setOnClickListener(list);
                imageViews[25].setOnClickListener(list);
                imageViews[26].setOnClickListener(list);
                imageViews[27].setOnClickListener(list);
                imageViews[28].setOnClickListener(list);
                imageViews[29].setOnClickListener(list);
                imageViews[30].setOnClickListener(list);
                imageViews[31].setOnClickListener(list);
                imageViews[32].setOnClickListener(list);
                imageViews[33].setOnClickListener(list);
                imageViews[34].setOnClickListener(list);
                imageViews[35].setOnClickListener(list);
                break;

        }
    }

    private void superFuncion(int i) {
        if (!cartaGirada[i]) {
            int x = combinations[i];
            if (!unaGirada) {
                coolImageFlipper.flipImage(drawables[x], imageViews[i]);
                unaGirada = true;
                cartaGirada[i] = true;
                primeraCarta = i;
            }
            else {
                coolImageFlipper.flipImage(drawables[x], imageViews[i]);
                segundaCarta = i;
                if (combinations[primeraCarta] == x) { // Acierto
                    cartaGirada[segundaCarta] = true;
                    progressBar.setProgress(progressBar.getProgress()+1);
                }
                else { // Fallo
                    MiTarea miTarea = new MiTarea();
                    miTarea.execute(-1, 1000);
                    cartaGirada[primeraCarta] = false;
                    cartaGirada[segundaCarta] = false;
                }
                unaGirada = false;
                ++intentosInt;
            }

        }
    }

    private void setCardsMemory() {

        for (int i = 0; i < 36; ++i) {
            combinations[i] = -1;
            cartaGirada[i] = false;
        }

        int a;
        for (int i = 0; i < 18; ++i) {
            do {
                a = (int )(Math.random() * 36);
            } while (combinations[a] != -1);
            combinations[a] = i;
            do {
                a = (int )(Math.random() * 36);
            } while (combinations[a] != -1);
            combinations[a] = i;
        }
    }

    private void gameWin() {
        try {
            PartyControllerImpl.getInstance().cancelParty();
        } catch (NoSuchPartyGoingOnException ignored) {}
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void retry() {
        for (int i = 0; i < 36; ++i) {
            MiTarea miTarea = new MiTarea();
            miTarea.execute(i, 50);
            combinations[i] = -1;
            cartaGirada[i] = false;
        }

        int a;
        for (int i = 0; i < 18; ++i) {
            do {
                a = (int )(Math.random() * 36);
            } while (combinations[a] != -1);
            combinations[a] = i;
            do {
                a = (int )(Math.random() * 36);
            } while (combinations[a] != -1);
            combinations[a] = i;
        }
        setViewsMemory(3);
        intentosInt = 0;
        intentosTV.setText(0 + "");
        progressBar.setProgress(0);
        unaGirada = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.memory_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.retry_item:
                retry();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MiTarea extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(params[1], 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (params[0] == -1) return "partida";
            else return params[0].toString();

        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("partida")) {
                coolImageFlipper.flipImage(getDrawable(R.drawable.question_mark), imageViews[segundaCarta]);
                coolImageFlipper.flipImage(getDrawable(R.drawable.question_mark), imageViews[primeraCarta]);
                for (int i = 0; i < 36; ++i) imageViews[i].setOnClickListener(list);
                primeraCarta = -1;
                segundaCarta = -1;
            }
            else {
                int i = Integer.parseInt(result);
                coolImageFlipper.flipImage(getDrawable(R.drawable.question_mark), imageViews[i]);
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPreExecute() {
            for (int i = 0; i < 36; ++i) imageViews[i].setOnClickListener(nulo);
            super.onPreExecute();
        }
    }
}
