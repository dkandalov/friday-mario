package fridaymario.sounds

import com.intellij.openapi.util.io.StreamUtil
import java.io.IOException

class Sounds private constructor(load: (Config) -> Sound) {
    val oneUp: Sound
    val oneDown: Sound
    val coin: Sound
    val bowserfalls: Sound
    val breakblock: Sound
    val fireball: Sound
    val fireworks: Sound
    val gameover: Sound
    val jumpSmall: Sound
    val jumpSuper: Sound
    val kick: Sound
    val stomp: Sound
    val powerupAppears: Sound
    val powerup: Sound
    val marioSong: Sound
    val zeldaSong: Sound

    private class Config(val filePath: String, val isBackgroundMusic: Boolean = false)

    companion object {
        fun create(actionSoundsEnabled: Boolean, backgroundMusicEnabled: Boolean): Sounds =
            Sounds {
                val enabled = it.isBackgroundMusic && backgroundMusicEnabled || !it.isBackgroundMusic && actionSoundsEnabled
                if (enabled) Sound(loadBytes(it.filePath), it.filePath)
                else SilentSound(loadBytes(it.filePath), it.filePath, SilentSound.Listener.none)
            }

        fun createSilent(listener: SilentSound.Listener) =
            Sounds { SilentSound(loadBytes(it.filePath), it.filePath, listener) }

        private fun loadBytes(fileName: String) =
            try {
                val inputStream = Sounds::class.java.getResourceAsStream(fileName) ?: error("Cannot find $fileName")
                StreamUtil.loadFromStream(inputStream)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
    }

    init {
        oneUp = load(Config("/fridaymario/sounds/smb_1-up.au"))
        oneDown = load(Config("/fridaymario/sounds/smb_pipe.au"))
        coin = load(Config("/fridaymario/sounds/smb_coin.au"))
        bowserfalls = load(Config("/fridaymario/sounds/smb_bowserfalls.au"))
        breakblock = load(Config("/fridaymario/sounds/smb_breakblock.au"))
        fireball = load(Config("/fridaymario/sounds/smb_fireball.au"))
        fireworks = load(Config("/fridaymario/sounds/smb_fireworks.au"))
        gameover = load(Config("/fridaymario/sounds/smb_gameover.au", isBackgroundMusic = true))
        jumpSmall = load(Config("/fridaymario/sounds/smb_jump-small.au"))
        jumpSuper = load(Config("/fridaymario/sounds/smb_jump-super.au"))
        kick = load(Config("/fridaymario/sounds/smb_kick.au"))
        stomp = load(Config("/fridaymario/sounds/smb_stomp.au"))
        powerup = load(Config("/fridaymario/sounds/smb_powerup.au"))
        powerupAppears = load(Config("/fridaymario/sounds/smb_powerup_appears.au"))
        marioSong = load(Config("/fridaymario/sounds/mario_08.au", isBackgroundMusic = true))
        zeldaSong = load(Config("/fridaymario/sounds/zelda_04.au", isBackgroundMusic = true))
    }
}