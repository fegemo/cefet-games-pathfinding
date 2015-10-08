package br.cefetmg.games;

import br.cefetmg.games.graphics.AgentRenderer;
import br.cefetmg.games.graphics.GraphRenderer;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class RunToTheHills extends ApplicationAdapter {

    private TiledMap tiledMap;

    private Viewport viewport;
    private OrthographicCamera camera;

    private TiledMapRenderer tiledMapRenderer;
    private GraphRenderer graphRenderer;

    private Agent agent;
    private AgentRenderer agentRenderer;

    private boolean debugMode = false;

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.translate(w / 2, h / 2);
        camera.update();
        viewport = new ScreenViewport(camera);

        // Carrega o mapa
        tiledMap = LevelManager.LoadLevel("forest-hills.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        graphRenderer = new GraphRenderer(camera);

        agentRenderer = new AgentRenderer(camera);
        agent = new Agent(
                new Vector2(
                        LevelManager.tileWidth / 2,
                        (float) Math.random() * LevelManager.totalPixelHeight),
                Color.FIREBRICK
        );

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.LEFT) {
                    camera.translate(-32, 0);
                }
                if (keycode == Input.Keys.RIGHT) {
                    camera.translate(32, 0);
                }
                if (keycode == Input.Keys.UP) {
                    camera.translate(0, -32);
                }
                if (keycode == Input.Keys.DOWN) {
                    camera.translate(0, 32);
                }
                if (keycode == Input.Keys.NUM_1) {
                    tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
                }
                if (keycode == Input.Keys.NUM_2) {
                    tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
                }
                if (keycode == Input.Keys.D) {
                    debugMode = !debugMode;
                }
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                Vector2 clique = new Vector2(x, y);
                viewport.unproject(clique);

                // Botão ESQUERDO: posiciona objetivo
                if (button == Input.Buttons.LEFT) {
//                    target.coords = clique;
                    agent.setGoal((int) clique.x, (int) clique.y);
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }

        });
    }

    /**
     * Atualiza o mundo virtual para ter as mesmas proporções que a janela.
     *
     * @param w Largura da janela.
     * @param h Altura da janela.
     */
    @Override
    public void resize(int w, int h) {
        viewport.update(w, h);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        agent.update(Gdx.graphics.getDeltaTime());

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        agentRenderer.render(agent);

        if (debugMode) {
            graphRenderer.renderGraph(LevelManager.graph);
        }
    }

}
