package br.cefetmg.games.movement.behavior;

import br.cefetmg.games.movement.Position;
import br.cefetmg.games.movement.Steering;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class Seek extends Algorithm {

    private static final char NAME = 's';
    
    public Seek(float maxSpeed) {
        this(NAME, maxSpeed);
    }
    
    protected Seek(char name, float maxSpeed) {
        super(name);
        this.maxSpeed = maxSpeed;
    }

    @Override
    public Steering steer(Position agent) {
        Steering output = new Steering();

        // Calcula a direção
        output.velocity = new Vector2(target.coords);
        output.velocity.sub(agent.coords);

        
        if (output.velocity.len2() > 0) {
            // normaliza a velocidade (fica |v| = 1)
            output.velocity.nor();
            // multiplica velovidade pela velocidade tangencial
            output.velocity.scl(maxSpeed);
        }

        return output;
    }
}
