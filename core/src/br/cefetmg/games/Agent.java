package br.cefetmg.games;

import br.cefetmg.games.movement.Position;
import br.cefetmg.games.movement.Steering;
import br.cefetmg.games.movement.Target;
import br.cefetmg.games.movement.behavior.Algorithm;
import br.cefetmg.games.movement.behavior.Seek;
import br.cefetmg.games.pathfinding.TileConnection;
import br.cefetmg.games.pathfinding.TileNode;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import java.util.Iterator;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class Agent {

    public Position position;
    private Algorithm behavior;
    private final IndexedAStarPathFinder pathFinder;
    private final DefaultGraphPath<TileConnection> path;
    private Iterator<TileConnection> pathIterator;
    private final Target steeringTarget;
    private final float fullSpeed = 75;
    private static final float MIN_DISTANCE_CONSIDERED_ZERO_SQUARED = (float) Math.pow(2.0f, 2);

    public Color color;

    public Agent(Vector2 position, Color color) {
        this.position = new Position(position);
        this.color = color;
        this.steeringTarget = new Target(position);
        this.behavior = new Seek(fullSpeed);
        this.behavior.target = steeringTarget;
        this.pathFinder = new IndexedAStarPathFinder(LevelManager.graph, false);
        this.path = new DefaultGraphPath<>();
        this.pathIterator = this.path.iterator();
    }

    public void update(float delta) {
        boolean shouldMove = true;

        // verifica se atingimos nosso objetivo imediato
        if (position.coords.dst2(steeringTarget.coords) < MIN_DISTANCE_CONSIDERED_ZERO_SQUARED) {
            // procurar se temos outra conexão na nossa rota
            // e, caso afirmativo, definir o nó de chegada como novo target
            if (shouldMove = pathIterator.hasNext()) {
                TileConnection nextConnection = pathIterator.next();
                TileNode nextNode = nextConnection.getToNode();
                steeringTarget.coords = nextNode.getPosition();

                // atualiza a velocidade do "seek" de acordo com o terreno (a conexão)
                this.behavior.maxSpeed = fullSpeed - (fullSpeed / 2.0f) * (nextConnection.getCost() - 1) / (LevelManager.maxCost - 1);
            }
        }

        // integra
        if (shouldMove) {
            Steering steering = behavior.steer(this.position);
            position.integrate(steering, delta);
        }
    }

    /**
     * Este método é chamado quando um clique no mapa é realizado.
     * @param x coordenada x do ponteiro do mouse.
     * @param y coordenada y do ponteiro do mouse.
     */
    public void setGoal(int x, int y) {
        TileNode startNode = LevelManager.graph.getNodeAtCoordinates((int) this.position.coords.x, (int) this.position.coords.y);
        TileNode targetNode = LevelManager.graph.getNodeAtCoordinates(x, y);

        path.clear();
        
        // AQUI ESTAMOS CHAMANDO O ALGORITMO A* (instância pathFinder)
        pathFinder.searchConnectionPath(startNode, targetNode, new Heuristic<TileNode>() {

            @Override
            public float estimate(TileNode n, TileNode n1) {
                throw new UnsupportedOperationException("Deveria ter retornado um valor para a heurística no arquivo Agent.java:85, mas o professor resolveu explodir o programa e deixar você consertar ;)");
            }
        }, path);
        pathIterator = path.iterator();
    }

    public void setBehavior(Algorithm behavior) {
        this.behavior = behavior;
    }

    public Algorithm getBehavior() {
        return behavior;
    }

    public char getBehaviorName() {
        return behavior != null ? behavior.name : '-';
    }
}