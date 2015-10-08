package br.cefetmg.games.movement;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Flávio Coutinho
 */
public class Position {

    public Vector2 coords;

    public Position() {
        this(new Vector2(0, 0));
    }

    public Position(Vector2 position) {
        this.coords = position;
    }

    public void integrate(Steering steering, float delta) {
        coords.x += steering.velocity.x * delta;
        coords.y += steering.velocity.y * delta;
    }
}
