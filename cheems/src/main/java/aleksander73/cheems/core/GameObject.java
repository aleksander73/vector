package aleksander73.cheems.core;

import java.util.ArrayList;
import java.util.List;

import aleksander73.cheems.rendering.renderers.Renderer;
import aleksander73.cheems.scene.Scene;

public class GameObject implements Script {
    private String name;
    private List<Component> components = new ArrayList<>();
    private Scene scene;
    private boolean active = true;

    protected GameObject(String name) {
        this.name = name;
    }

    @Override
    public void start() {}

    @Override
    public void update() {}

    public static void instantiate(final GameObject gameObject) {
        final Scene currentScene = Scene.getCurrentScene();
        currentScene.getOnUpdated().queueRunnable(new Runnable() {
            @Override
            public void run() {
                currentScene.addGameObject(gameObject);
                gameObject.start();
            }
        });
    }

    public void destroy() {
        this.setActive(false);
        Renderer renderer = this.getComponent(Renderer.class);
        if(renderer != null) {
            renderer.setActive(false);
        }
        final Scene currentScene = Scene.getCurrentScene();
        currentScene.getOnUpdated().queueRunnable(new Runnable() {
            @Override
            public void run() {
                currentScene.removeGameObject(GameObject.this);
            }
        });
    }

    public String getName() {
        return name;
    }

    public <T extends Component> T getComponent(Class<T> type) {
        T result = null;
        for(Component component : components) {
            if(type.isInstance(component)) {
                result = (T)component;
                break;
            }
        }

        return result;
    }

    public <T extends Component> List<T> getComponents(Class<T> type) {
        List<T> result = new ArrayList<>();
        for(Component component : components) {
            if(type.isInstance(component)) {
                result.add((T)component);
            }
        }

        return result;
    }

    public void addComponent(Component component) {
        components.add(component);
        component.setGameObject(this);
        component.initialize();
    }

    public void addComponents(Component... components) {
        for(Component component : components) {
            this.addComponent(component);
        }
    }

    public <T extends Component> void removeComponent(Component component) {
        components.remove(component);
    }

    public <T extends Component> void removeComponents(List<Component> components) {
        this.components.removeAll(components);
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
