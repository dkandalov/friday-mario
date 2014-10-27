package audible.sounds;

import com.intellij.openapi.diagnostic.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

public class Sound {
    private static final Logger LOG = Logger.getInstance(Sound.class);

    private final byte[] bytes;
    private final String name;
    private final AtomicReference<Clip> clipReference = new AtomicReference<Clip>();

    public Sound(byte[] bytes, String name) {
        this.bytes = bytes;
        this.name = name;
    }

    public Sound play() {
        playSoundFromStream(new ByteArrayInputStream(bytes), 0);
        return this;
    }

    public Sound playAndWait() {
        playSoundFromStream(new ByteArrayInputStream(bytes), 0);

        Clip clip = clipReference.get();
        if (clip != null) {
            try {
                Thread.sleep(clip.getMicrosecondLength());
            } catch (InterruptedException ignored) {
            }
        }
        return this;
    }

    public Sound playInBackground() {
        playSoundFromStream(new ByteArrayInputStream(bytes), Clip.LOOP_CONTINUOUSLY);
        return this;
    }

    public Sound stop() {
        Clip clip = clipReference.get();
        if (clip != null) clip.stop();
        return this;
    }

    @Override
    public String toString() {
        return "Sound{name='" + name + '\'' + '}';
    }

    /**
     * Originally copied from {@link com.intellij.util.ui.UIUtil}.
     */
    private void playSoundFromStream(final InputStream inputStream, final int loopCount) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the Clip finishing;
            @Override
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    InputStream stream = inputStream;
                    if (!stream.markSupported()) stream = new BufferedInputStream(stream);
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(stream);
                    clip.open(inputStream);
                    clip.loop(loopCount);
                    clipReference.set(clip);
                } catch (Exception e) {
                    // TODO javax.sound.sampled.LineUnavailableException: No Free Voices
                    LOG.warn(e);
                }
            }
        }).start();
    }
}
