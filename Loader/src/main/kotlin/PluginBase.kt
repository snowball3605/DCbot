package snow

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import java.util.EventListener

abstract class PluginBase {
    abstract fun onStart(jda: JDA)

    open fun Commands(): List<CommandData> = emptyList()
    open fun EventListeners(): List<ListenerAdapter> = emptyList()
}