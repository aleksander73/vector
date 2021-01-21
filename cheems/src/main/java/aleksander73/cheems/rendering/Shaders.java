package aleksander73.cheems.rendering;

import aleksander73.cheems.rendering.shaders.GUIShader;
import aleksander73.cheems.rendering.shaders.Shader;
import aleksander73.cheems.rendering.shaders.SkyboxShader;
import aleksander73.cheems.rendering.shaders.StandardShader;

public class Shaders {
    private static Shader standardShader;
    private static Shader guiShader;
    private static Shader skyboxShader;

    public static void initShaders() {
        standardShader = new StandardShader();
        guiShader = new GUIShader();
        skyboxShader = new SkyboxShader();
    }

    public static Shader getStandardShader() {
        return standardShader;
    }

    public static Shader getGuiShader() {
        return guiShader;
    }

    public static Shader getSkyboxShader() {
        return skyboxShader;
    }
}
