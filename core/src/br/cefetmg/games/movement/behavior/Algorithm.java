package br.cefetmg.games.movement.behavior;

import br.cefetmg.games.movement.Position;
import br.cefetmg.games.movement.Target;
import br.cefetmg.games.movement.Steering;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public abstract class Algorithm {

    public float maxSpeed;
    public Target target;
    public char name;

    public Algorithm(char name) {
        this(name, false);
    }

    public Algorithm(char nome, boolean protege) {
        this.name = nome;
    }

    public abstract Steering steer(Position agent);
}
