package snow

import net.dv8tion.jda.api.JDA

abstract class PluginBase {
    abstract fun onStart(jda: JDA)
}