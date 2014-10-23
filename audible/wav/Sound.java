package audible.wav;

import static liveplugin.PluginUtil.show;

public class Sound {
    private final String name;
    private final byte[] bytes;

    public Sound(String name, byte[] bytes) {
        this.name = name;
        this.bytes = bytes;
    }

    public void play() {
        show(name);
//        UIUtil.doPlay(new com.intellij.openapi.util.Factory<InputStream>() {
//            @Override InputStream create() {
//                new ByteArrayInputStream(bytes)
//            }
//        })
    }
}
