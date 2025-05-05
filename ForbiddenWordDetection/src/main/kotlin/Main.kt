package snow

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.yaml.snakeyaml.Yaml
import snow.Main.Config.config
import snow.Main.Config.text
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class Main : PluginBase() {
    object Config {
        val config = File("Plug-in/ForbiddenWordDetection/word.yml")
        val Yaml = Yaml()
        var config_ = Yaml.load(FileInputStream(config)) as Map<String, Any>
        var text = config_["Word"] as List<String>
    }

    override fun onStart(jda: JDA) {
        val folder = File("Plug-in/ForbiddenWordDetection")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val inputStream = this::class.java.classLoader.getResourceAsStream("word.yml") ?: throw FileNotFoundException("word.yml does not exist in resources")
        if (!config.exists()) {
            config.outputStream().use { inputStream.copyTo(it) }
        }
    }

    override fun EventListeners(): List<ListenerAdapter> {  
        return listOf(
            object : ListenerAdapter() {
                override fun onMessageReceived(event: MessageReceivedEvent) {
                    for (item in text) {
                        if (event.message.toString().contains(item, ignoreCase = true)) {
                            event.message.delete().queue()
                            event.channel.sendMessage("I have deleted the banned words").queue()
                        }
                    }
                }
            },
            object : ListenerAdapter() {
                override fun onMessageUpdate(event: MessageUpdateEvent) {
                    for (item in text) {
                        if (event.message.toString().contains(item, ignoreCase = true)) {
                            event.message.delete().queue()
                            event.channel.sendMessage("I have deleted the banned words").queue()
                        }
                    }
                }
            }
        )
    }
}