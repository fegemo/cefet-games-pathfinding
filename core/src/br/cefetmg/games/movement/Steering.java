package br.cefetmg.games.movement;

import com.badlogic.gdx.math.Vector2;

/**
 * SteeringOutput é um direcionamento que será feito por um agente.
 *
 * Ele consiste de uma componente linear e outra angular (velocidade e rotação)
 *
 * @author Flávio Coutinho
 */
public class Steering {

    public Vector2 velocity;

    public Steering() {
        velocity = new Vector2();
    }
}
