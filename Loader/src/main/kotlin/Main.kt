package snow

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.requests.GatewayIntent
import org.yaml.snakeyaml.Yaml
import snow.PluginManager.plugins
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URLClassLoader
import java.util.*
import java.util.jar.JarFile


fun main() {

    var config = File("config.yml")
    val inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.yml")
    if (!config.exists()) {
        config.outputStream().use { inputStream.copyTo(it) }
    } else {
        var yaml = Yaml()

        var config_ = yaml.load(FileInputStream(File("config.yml"))) as Map<String, Any>


        val jda = JDABuilder.createDefault(config_["Token"].toString())
            .enableIntents(GatewayIntent.GUILD_MESSAGES)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
            .build()

        jda.awaitReady()
        PluginManager.loadPlugins(File("Plug-in"), jda)

    }
}

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
object PluginManager {
    private val plugins = mutableListOf<PluginBase>()

    fun loadPlugins(pluginsDir: File, jda: JDA) {
        if (!pluginsDir.exists()) {
            pluginsDir.mkdirs()
            println("Created plugins directory: ${pluginsDir.path}")
            return
        }

        val commands = mutableListOf<CommandData>()
        val eventListeners = mutableListOf<ListenerAdapter>()

        pluginsDir.listFiles { file -> file.extension == "jar" }?.forEach { jarFile ->
            try {
                val jar = JarFile(jarFile)
                val entry = jar.getEntry("plugin.yml")

                if (entry == null) {
                    println("No plugin.yml found in ${jarFile.name}")
                }
                    val inputStream: InputStream = jar.getInputStream(entry)
                    val yaml = Yaml()
                    val config = yaml.load<Map<String, Any>>(inputStream)

                    val mainClass = config["main"] as? String
                    if (mainClass == null) {
                        println("No class specified in plugin.yml for ${jarFile.name}")
                    }
                        val classLoader = URLClassLoader(arrayOf(jarFile.toURI().toURL()))
                        val pluginClass = classLoader.loadClass(mainClass)

                        if (!PluginBase::class.java.isAssignableFrom(pluginClass)) {
                            println("${pluginClass.simpleName} does not extend PluginBase")
                        }
                            val pluginInstance = pluginClass.getDeclaredConstructor().newInstance() as PluginBase
                            plugins.add(pluginInstance)
                            commands.addAll(pluginInstance.Commands())
                            eventListeners.addAll(pluginInstance.EventListeners())
                            pluginInstance.onStart(jda)
                            println("Loaded plugin: ${config["name"]} (Version: ${config["version"]})")

                jda.updateCommands().addCommands(commands).queue()
                eventListeners.forEach { jda.addEventListener(it) }
            } catch (e: Exception) {
                println("Failed to load plugin from ${jarFile.name}: ${e.message}")
            }
        }
    }
}