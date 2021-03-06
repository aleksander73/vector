package aleksander73.vector.physics;

import java.util.HashMap;
import java.util.Map;

import aleksander73.vector.adt.Queue;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.core.GameObject;
import aleksander73.vector.core.System;
import aleksander73.vector.core.Transform;
import aleksander73.vector.physics.collision.Collider;
import aleksander73.vector.physics.collision.Collision;
import aleksander73.vector.scene.Scene;
import aleksander73.vector.time.Time;
import aleksander73.vector.utility.ListUtility;
import aleksander73.vector.utility.functional_interface.Condition;
import aleksander73.vector.utility.functional_interface.Consumer;
import aleksander73.math.linear_algebra.Vector3d;

public class PhysicsSystem extends System {
    private final Condition<GameObject> isPhysicalBody;
    private float gravityScaleFactor = 1.0f;
    private static final float STANDARD_G = 9.81f;

    private final Condition<GameObject> isCollidable;
    private final Map<GameObject, Vector3d> prevPositions = new HashMap<>();
    private final Queue<Collision> collisions = new Queue<>();

    public PhysicsSystem(GameEngine gameEngine) {
        super(gameEngine);
        isPhysicalBody = new Condition<GameObject>() {
            @Override
            public boolean test(GameObject gameObject) {
                return gameObject.getComponent(Rigidbody.class) != null;
            }
        };
        isCollidable = new Condition<GameObject>() {
            @Override
            public boolean test(GameObject gameObject) {
                return gameObject.getComponent(Collider.class) != null;
            }
        };
        this.setReady(true);
    }

    public void gatherInformation(Scene scene) {
        for(GameObject gameObject : ListUtility.filter(scene.getGameObjects(), isCollidable)) {
            Vector3d position = gameObject.getComponent(Transform.class).getPosition();
            prevPositions.put(gameObject, position);
        }
    }

    public void simulatePhysics(Scene scene) {
        for(GameObject gameObject : ListUtility.filter(scene.getGameObjects(), isPhysicalBody)) {
            Rigidbody rigidbody = gameObject.getComponent(Rigidbody.class);
            if(rigidbody.isGravityApplied()) {
                float deltaVelocity = this.getG() * Time.getDeltaTime();
                float newVelocity = rigidbody.getVelocity() + deltaVelocity;
                rigidbody.setVelocity(newVelocity);
                Transform transform = gameObject.getComponent(Transform.class);
                transform.translate(Vector3d.yUnitVector.negate().mul(newVelocity).toVector3d());
            }
        }

        for(Collision collision : collisions) {
            GameObject gameObject1 = collision.getGameObject1();
            GameObject gameObject2 = collision.getGameObject2();

            Collider collider1 = gameObject1.getComponent(Collider.class);
            boolean skipDefault1 = false;
            Map<Condition<GameObject>, Consumer<GameObject>> map1 = collider1.getCustomOnCollisionEnter();
            for(Condition<GameObject> condition : map1.keySet()) {
                if(condition.test(gameObject2)) {
                    map1.get(condition).consume(gameObject2);
                    skipDefault1 = true;
                }
            }

            Collider collider2 = gameObject2.getComponent(Collider.class);
            boolean skipDefault2 = false;
            Map<Condition<GameObject>, Consumer<GameObject>> map2 = collider2.getCustomOnCollisionEnter();
            for(Condition<GameObject> condition : map2.keySet()) {
                if(condition.test(gameObject1)) {
                    map2.get(condition).consume(gameObject1);
                    skipDefault2 = true;
                }
            }

            if(!skipDefault1) {
                Vector3d prevPosition1 = prevPositions.get(gameObject1);
                if(prevPosition1 != null) {
                    gameObject1.getComponent(Transform.class).setPosition(prevPosition1);
                }
            }
            if(!skipDefault2) {
                Vector3d prevPosition2 = prevPositions.get(gameObject2);
                if(prevPosition2 != null) {
                    gameObject2.getComponent(Transform.class).setPosition(prevPosition2);
                }
            }
        }
        collisions.clear();
    }

    public void queueCollision(Collision collision) {
        boolean pass = prevPositions.containsKey(collision.getGameObject1()) && prevPositions.containsKey(collision.getGameObject2());
        if(pass) {
            collisions.add(collision);
        }
    }

    public void setGravityScaleFactor(float gravityScaleFactor) {
        this.gravityScaleFactor = gravityScaleFactor;
    }

    public float getG() {
        return STANDARD_G * gravityScaleFactor;
    }
}
