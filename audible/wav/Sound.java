package audible.wav;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

public class Sound {
    private final byte[] bytes;
    private final AtomicReference<Clip> clipReference = new AtomicReference<Clip>();

    public Sound(byte[] bytes) {
        this.bytes = bytes;
    }

    public Sound play() {
        playSoundFromStream(new ByteArrayInputStream(bytes), 0);
        return this;
    }

    public Sound stop() {
        Clip clip = clipReference.get();
        if (clip != null) clip.stop();
        return this;
    }

    public Sound playInBackground() {
        playSoundFromStream(new ByteArrayInputStream(bytes), Clip.LOOP_CONTINUOUSLY);
        return this;
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
                } catch (Exception ignore) {
                }
            }
        }).start();
    }
}
