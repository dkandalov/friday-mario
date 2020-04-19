package fridaymario.sounds

import com.intellij.openapi.diagnostic.Logger
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.concurrent.atomic.AtomicReference
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

open class Sound(private val bytes: ByteArray, private val name: String) {
    private var clip: Clip? = null

    open fun play() {
        playSoundFromStream(ByteArrayInputStream(bytes))
    }

    open fun playAndWait() {
        playSoundFromStream(ByteArrayInputStream(bytes))
        if (clip != null) {
            try {
                Thread.sleep(clip!!.microsecondLength / 1000)
            } catch (ignored: InterruptedException) {
            }
        }
    }

    open fun playInBackground() {
        playSoundFromStream(ByteArrayInputStream(bytes), Clip.LOOP_CONTINUOUSLY)
    }

    open fun stop() {
        clip?.stop()
    }

    /**
     * Originally copied from [com.intellij.util.ui.UIUtil].
     */
    private fun playSoundFromStream(inputStream: InputStream, loopCount: Int = 0) {
        try {
            val clip = AudioSystem.getClip()
            var stream = inputStream
            if (!stream.markSupported()) stream = BufferedInputStream(stream)
            clip.open(AudioSystem.getAudioInputStream(stream))
            val lineListener = AtomicReference<LineListener>()
            val listener = LineListener { event: LineEvent ->
                if (event.type === LineEvent.Type.STOP) {
                    clip.close()
                    clip.removeLineListener(lineListener.get())
                }
            }
            lineListener.set(listener)
            clip.addLineListener(lineListener.get())
            this.clip = clip

            // The wrapper thread is unnecessary, unless it blocks on the Clip finishing;
            Thread(Runnable { clip.loop(loopCount) }).start()
        } catch (e: Exception) {
            logger.warn(e)
        }
    }

    override fun toString() = "Sound{name='$name'}"

    companion object {
        private val logger = Logger.getInstance(Sound::class.java)
    }
}