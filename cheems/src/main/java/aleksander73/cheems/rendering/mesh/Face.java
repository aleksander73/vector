package aleksander73.cheems.rendering.mesh;

public class Face {
    public static final int VERTICES_COUNT = 3;
    private Vertex[] vertices;

    public Face(Vertex[] vertices) {
        this.vertices = vertices;
    }

    public Vertex[] getVertices() {
        return vertices;
    }
}
