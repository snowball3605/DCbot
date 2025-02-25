package snow

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.InputStream
import java.net.URLClassLoader
import java.util.jar.JarFile


fun main() {
    PluginManager.loadPlugins(File("Plug-in"))
    PluginManager.enablePlugins()
}

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
object PluginManager {
    private val plugins = mutableListOf<PluginBase>()

    fun loadPlugins(pluginsDir: File) {
        if (!pluginsDir.exists()) {
            pluginsDir.mkdirs()
            println("Created plugins directory: ${pluginsDir.path}")
            return
        }

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
                        println("No 'main' class specified in plugin.yml for ${jarFile.name}")
                    }
                        val classLoader = URLClassLoader(arrayOf(jarFile.toURI().toURL()))
                        val pluginClass = classLoader.loadClass(mainClass)

                        if (!PluginBase::class.java.isAssignableFrom(pluginClass)) {
                            println("${pluginClass.simpleName} does not extend PluginBase")
                        }
                            val pluginInstance = pluginClass.getDeclaredConstructor().newInstance() as PluginBase
                            plugins.add(pluginInstance)
                            println("Loaded plugin: ${config["name"]} (Version: ${config["version"]})")

            } catch (e: Exception) {
                println("Failed to load plugin from ${jarFile.name}: ${e.message}")
            }
        }
    }

    fun enablePlugins() {
        plugins.forEach { it.onStart() }
    }
}