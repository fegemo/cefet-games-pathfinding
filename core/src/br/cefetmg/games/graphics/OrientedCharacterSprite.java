package br.cefetmg.games.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;

/**
 * Uma sprite que pode estar orientada em 8 direções e possui 3 quadros de 
 * animação de "andando" em cada uma delas.
 * 
 * É criada a partir de uma sprite sheet contendo 8x3 quadros, em que cada
 * coluna tem 3 quadros de animação do personagem andando em uma direção.
 * 
 * As direções são NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST,
 * NORTHWEST, nessa ordem (@see Facing).
 * 
 * @see Facing
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class OrientedCharacterSprite extends Sprite {

    private static final float FRAME_DURATION = 0.2f;
    private final HashMap<Facing, Animation> animations;
    private Animation currentAnimation;
    private float animationTime;
    private boolean moving;

    /**
     * Constrói uma sprite com a sprite sheet texture, em que cada quadro
     * tem dimensões frameWidth e frameHeight.
     * 
     * @param texture sprite sheet.
     * @param frameWidth largura de cada quadro.
     * @param frameHeight algura de cada quadro.
     */
    public OrientedCharacterSprite(Texture texture, int frameWidth,
            int frameHeight) {
        TextureRegion[][] frames = TextureRegion.split(
                texture, frameWidth, frameHeight);

        super.setSize(frameWidth, frameHeight);
        animations = new HashMap<>();
        for (int j = 0; j < Facing.values().length; j++) {
            // quadros: 0 -> parado, 1 -> pé direito, 2 -> pé esquerdo
            // sequência de andando: 0, 1, 0, 2
            animations.put(Facing.values()[j], new Animation(FRAME_DURATION,
                    frames[0][j],
                    frames[1][j],
                    frames[0][j],
                    frames[2][j]
            ));
            animations.get(Facing.values()[j])
                    .setPlayMode(Animation.PlayMode.LOOP);
        }
        currentAnimation = animations.get(Facing.SOUTH);
        super.setTexture(currentAnimation.getKeyFrame(0).getTexture());
        animationTime = 0;
        moving = false;
    }

    /**
     * Atualiza a animação da sprite.
     * @param dt tempo desde o último quadro.
     */
    public void update(float dt) {
        animationTime += dt;
        if (animationTime > 100) {
            animationTime -= 100;
        }
    }

    /**
     * Desenha esta sprite usando o quadro atual da animação corrente.
     * @param batch 
     */
    @Override
    public void draw(Batch batch) {
        TextureRegion currentFrame = moving
                ? currentAnimation.getKeyFrame(animationTime)
                : currentAnimation.getKeyFrames()[0];
        batch.draw(currentFrame, super.getX(), super.getY());
    }

    /**
     * Define qual a orientação da sprite.
     * @param orientation nova orientação.
     * @see Facing
     */
    public void setFacing(Facing orientation) {
        Animation newAnimation = animations.get(orientation);
        if (currentAnimation != newAnimation) {
            currentAnimation = newAnimation;
            animationTime = 0;
        }
    }

    /**
     * Define se a sprite está com animação de se movendo ou se ela está 
     * parada (sem animar).
     * @param moving se está se movimentando e animando.
     */
    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}
