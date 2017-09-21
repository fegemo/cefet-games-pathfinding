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

    // tamanho dos quadros da animação (40x40)
    private static final int FRAME_WIDTH = 40;
    private static final int FRAME_HEIGHT = FRAME_WIDTH;

    // um deslocamento em Y para que o personagem fique alinhado à altura 
    // da ponte
    private static final int POSITION_OFFSET_Y = FRAME_HEIGHT / 2;        // 20

    // quanto para baixo ele é desenhado quando na água
    private static final int POSITION_WATER_OFFSET_Y = FRAME_HEIGHT / 8;  // 5
    // quanto do corpo aparece molhado quanto está na água
    private static final int UNDERWATER_HEIGHT_PORTION = FRAME_HEIGHT / 4;// 10

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
        this.sprite = new OrientedCharacterSprite(character,
                FRAME_WIDTH, FRAME_HEIGHT);
    }

    public void render(Agent agent) {
        sprite.update(Gdx.graphics.getDeltaTime());
        sprite.setCenter(
                agent.position.coords.x,
                agent.position.coords.y);
        sprite.setFacing(agent.getFacing());
        sprite.setMoving(agent.isMoving());
        sprite.translateY(POSITION_OFFSET_Y);
        batch.setProjectionMatrix(camera.combined);

        // se o agente está debaixo d'água, para dar o efeito visual:
        //   1. desenhamos ele deslocado (translateY) um pouco para baixo
        //   2. desenhamos a metade de cima dele, normal
        //   3. desenhamos a metade de baixo com a cor azulada
        if (agent.isUnderWater()) {
            // verifica qtos % do trajeto para a água o agente está
            float currentDepth = agent.getUnderWaterLevel();
            // desloca para baixo
            sprite.translateY(-POSITION_WATER_OFFSET_Y * currentDepth);

            // vamos desenhar apenas a metade de cima do agente
            Gdx.gl20.glEnable(GL20.GL_SCISSOR_TEST);
            Gdx.gl20.glScissor(
                    (int) sprite.getX(),
                    (int) (sprite.getY() + UNDERWATER_HEIGHT_PORTION * currentDepth),
                    FRAME_WIDTH,
                    (int) (FRAME_HEIGHT - UNDERWATER_HEIGHT_PORTION * currentDepth)
            );
            batch.begin();
            sprite.draw(batch);
            batch.end();

            // agora, vamos desenhar só a parte de baixo, com a cor azulada
            Gdx.gl20.glScissor(
                    (int) sprite.getX(),
                    (int) sprite.getY(),
                    FRAME_WIDTH,
                    (int) (UNDERWATER_HEIGHT_PORTION * currentDepth));
            Gdx.gl.glEnable(GL20.GL_BLEND);
            batch.setColor(0, 0, 1, 0.5f);
            batch.begin();
            sprite.draw(batch);
            batch.end();
            batch.setColor(Color.WHITE);
            Gdx.gl.glDisable(GL20.GL_BLEND);
            Gdx.gl20.glDisable(GL20.GL_SCISSOR_TEST);
        } else {
            // desenha o agente normalmente, já que ele não está debaixo d'água
            batch.begin();
            sprite.draw(batch);
            batch.end();
        }
    }
}
