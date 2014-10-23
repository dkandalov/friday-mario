package audible.wav;

import com.intellij.openapi.util.Factory;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Sound {
    private final byte[] bytes;

    public Sound(byte[] bytes) {
        this.bytes = bytes;
    }

    public void play() {
        playSoundFromStream(new com.intellij.openapi.util.Factory<InputStream>() {
            @Override
            public InputStream create() {
                return new ByteArrayInputStream(bytes);
            }
        });
    }

    private static void playSoundFromStream(final Factory<InputStream> streamProducer) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            @Override
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    InputStream stream = streamProducer.create();
                    if (!stream.markSupported()) stream = new BufferedInputStream(stream);
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(stream);
                    clip.open(inputStream);

                    clip.start();
                } catch (Exception ignore) {
                }
            }
        }).start();
    }
}
