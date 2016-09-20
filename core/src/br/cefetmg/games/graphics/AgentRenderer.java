package br.cefetmg.games.graphics;

import br.cefetmg.games.Agent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Um renderizador de agentes que usa uma sprite animada com 8 direções.
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class AgentRenderer {

    private final SpriteBatch batch;
    private final Camera camera;
    private final OrientedCharacterSprite sprite;

    /**
     * Cria um novo renderizador com uma textura 8x3 (8 direções, 3 quadros de
     * animação de "andando" em cada uma).
     *
     * @param batch
     * @param camera
     * @param character
     */
    public AgentRenderer(SpriteBatch batch, Camera camera,
            Texture character) {
        this.batch = batch;
        this.camera = camera;
        this.sprite = new OrientedCharacterSprite(character, 40, 40);
    }

    public void render(Agent agent) {
        sprite.update(Gdx.graphics.getDeltaTime());
        sprite.setCenter(agent.position.coords.x, (int) agent.position.coords.y);
        sprite.setFacing(agent.getFacing());
        sprite.setMoving(agent.isMoving());
        batch.setProjectionMatrix(camera.combined);

        if (agent.isUnderWater()) {
            sprite.translateY(-5);
            Gdx.gl20.glEnable(GL20.GL_SCISSOR_TEST);
            Gdx.gl20.glScissor(
                    (int) agent.position.coords.x - 20,
                    (int) agent.position.coords.y - 10, 40, 30);
            batch.begin();
            sprite.draw(batch);
            batch.end();
            Gdx.gl20.glScissor(
                    (int) agent.position.coords.x - 20,
                    (int) agent.position.coords.y - 20, 40, 10);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            batch.setColor(0, 0, 1, 0.5f);
            batch.begin();
            sprite.draw(batch);
            batch.end();
            batch.setColor(Color.WHITE);
            Gdx.gl.glDisable(GL20.GL_BLEND);
            Gdx.gl20.glDisable(GL20.GL_SCISSOR_TEST);
        } else {
            batch.begin();
            sprite.draw(batch);
            batch.end();
        }
    }
}
