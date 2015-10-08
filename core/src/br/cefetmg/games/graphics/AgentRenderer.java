package br.cefetmg.games.graphics;

import br.cefetmg.games.Agent;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class AgentRenderer {

    private final ShapeRenderer shapeRenderer;
    private final Camera camera;
    
    public AgentRenderer(Camera camera) {
        shapeRenderer = new ShapeRenderer();
        this.camera = camera;
    }
    
    public void render(Agent agent) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(agent.color);
        shapeRenderer.translate(agent.position.coords.x, agent.position.coords.y, 0);
        shapeRenderer.circle(0, 0, 10);
        shapeRenderer.identity();
        shapeRenderer.end();
    }

}
