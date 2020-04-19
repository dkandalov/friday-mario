package fridaymario.sounds

class SilentSound(
    bytes: ByteArray,
    private val soundName: String,
    private val listener: Listener
): Sound(bytes, soundName) {

    override fun play() = listener.playing(soundName)
    override fun playAndWait() = listener.playing(soundName)
    override fun playInBackground() = listener.playing(soundName)
    override fun stop() = listener.stopped("stopped: $soundName")
    override fun toString() = "SilentLogSound{name='$soundName'}"

    interface Listener {
        fun playing(soundName: String)
        fun stopped(soundName: String)

        companion object {
            @JvmField
            val none: Listener = object: Listener {
                override fun playing(soundName: String) {}
                override fun stopped(soundName: String) {}
            }
        }
    }
}