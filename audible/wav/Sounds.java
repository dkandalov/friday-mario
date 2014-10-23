package audible.wav;

import com.intellij.openapi.util.io.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Sounds {
    private final Map<String, byte[]> cache = new HashMap<String, byte[]>();
    public final Sound coin;
    public final Sound bowserfalls;

    public Sounds() {
        try {
            coin = load("/audible/wav/smb_coin.wav");
            bowserfalls = load("/audible/wav/smb_bowserfalls.wav");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Sound load(String fileName) throws IOException {
        byte[] bytes = cache.get(fileName);
        if (bytes == null) {
            InputStream inputStream = Sounds.class.getResourceAsStream(fileName);
            if (inputStream == null) throw new RuntimeException("Cannot find " + fileName);

            bytes = StreamUtil.loadFromStream(inputStream);
            cache.put(fileName, bytes);
        }
        return new Sound(fileName, bytes);
    }
}

