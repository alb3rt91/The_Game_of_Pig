package com.example.thegameofpig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button btnLanzar, btnPasarTurno, btnReiniciar;
    private TextView tvPuntuacion1, tvPuntuacion2, tvAcumulado1, tvAcumulado2, tvGanador;
    private ImageView ivDado;
    private View viewJugador1, viewJugador2;
    private Random random;

    private int puntuacion1 = 0, puntuacion2 = 0;
    private int acumulado1 = 0, acumulado2 = 0; // Puntuación acumulada para cada jugador
    private boolean turnoJugador1 = true; // true si es el turno del jugador 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnBackToMenu = findViewById(R.id.btnBackToMenu);
        btnBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Regresar al menú principal
                Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish(); // Opcional: Finaliza la actividad actual para que no esté en la pila de actividades
            }
        });

        // Configurar la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicializar elementos de la interfaz
        btnLanzar = findViewById(R.id.btnLanzar);
        btnPasarTurno = findViewById(R.id.btnPasarTurno);
        btnReiniciar = findViewById(R.id.btnReiniciar);
        tvPuntuacion1 = findViewById(R.id.tvPuntuacion1);
        tvPuntuacion2 = findViewById(R.id.tvPuntuacion2);
        tvAcumulado1 = findViewById(R.id.tvAcumulado1);
        tvAcumulado2 = findViewById(R.id.tvAcumulado2);
        tvGanador = findViewById(R.id.tvGanador);
        ivDado = findViewById(R.id.ivDado);
        viewJugador1 = findViewById(R.id.viewJugador1);
        viewJugador2 = findViewById(R.id.viewJugador2);
        random = new Random();

        // Resaltar el jugador que va primero al inicio de la aplicación
        resaltarTurnoJugador();

        // Configurar los botones
        btnLanzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarDado();
            }
        });

        btnPasarTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarTurno();
            }
        });

        btnReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciarJuego();
            }
        });
    }

    private void lanzarDado() {
        int valorDado = random.nextInt(6) + 1;
        // Actualizar la imagen del dado según el valor generado
        int drawableId = getResources().getIdentifier("dice_" + valorDado, "drawable", getPackageName());
        ivDado.setImageResource(drawableId);

        // Manejar la lógica del juego al lanzar el dado
        if (valorDado == 1) {
            Toast.makeText(this, "¡Turno perdido! Acumulado se pierde.", Toast.LENGTH_SHORT).show();
            if (turnoJugador1) {
                acumulado1 = 0;
                tvAcumulado1.setText(String.valueOf(acumulado1));
            } else {
                acumulado2 = 0;
                tvAcumulado2.setText(String.valueOf(acumulado2));
            }
            pasarTurno();
        } else {
            if (turnoJugador1) {
                acumulado1 += valorDado;
                tvAcumulado1.setText(String.valueOf(acumulado1));
                verificarGanador(puntuacion1 + acumulado1, 50, 1);
            } else {
                acumulado2 += valorDado;
                tvAcumulado2.setText(String.valueOf(acumulado2));
                verificarGanador(puntuacion2 + acumulado2, 50, 2);
            }
        }
    }

    private void pasarTurno() {
        if (turnoJugador1) {
            puntuacion1 += acumulado1;
            acumulado1 = 0;
            tvAcumulado1.setText(String.valueOf(acumulado1));
        } else {
            puntuacion2 += acumulado2;
            acumulado2 = 0;
            tvAcumulado2.setText(String.valueOf(acumulado2));
        }

        // Cambiar el turno al otro jugador
        turnoJugador1 = !turnoJugador1;
        resaltarTurnoJugador();

        // Actualizar las puntuaciones mostradas
        tvPuntuacion1.setText("Jugador 1: " + puntuacion1);
        tvPuntuacion2.setText("Jugador 2: " + puntuacion2);
    }

    private void resaltarTurnoJugador() {
        if (turnoJugador1) {
            viewJugador1.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            viewJugador2.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        } else {
            viewJugador2.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            viewJugador1.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    private void verificarGanador(int puntuacion, int umbral, int jugador) {
        if (puntuacion >= umbral) {
            String mensajeGanador = "¡Gana el Jugador " + jugador + "!";
            tvGanador.setText(mensajeGanador);
            Toast.makeText(this, mensajeGanador, Toast.LENGTH_LONG).show();
            resaltarJugadorGanador(jugador);
            btnLanzar.setEnabled(false);
            btnPasarTurno.setEnabled(false);
        }
    }

    private void resaltarJugadorGanador(int jugador) {
        if (jugador == 1) {
            viewJugador1.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            viewJugador2.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        } else {
            viewJugador2.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            viewJugador1.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    private void reiniciarJuego() {
        puntuacion1 = 0;
        puntuacion2 = 0;
        acumulado1 = 0;
        acumulado2 = 0;
        tvPuntuacion1.setText("Jugador 1: 0");
        tvPuntuacion2.setText("Jugador 2: 0");
        tvAcumulado1.setText("0");
        tvAcumulado2.setText("0");
        tvGanador.setText("");
        ivDado.setImageResource(R.drawable.dice_1); // Restablecer la imagen del dado al valor inicial
        viewJugador1.setBackgroundColor(getResources().getColor(R.color.jugador1_color));
        viewJugador2.setBackgroundColor(getResources().getColor(R.color.jugador2_color));
        btnLanzar.setEnabled(true);
        btnPasarTurno.setEnabled(true);
        turnoJugador1 = true;
        resaltarTurnoJugador();
        Toast.makeText(this, "Juego reiniciado. ¡Que empiece el juego!", Toast.LENGTH_SHORT).show();
    }
}
