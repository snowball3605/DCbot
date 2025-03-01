package snow

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.yaml.snakeyaml.Yaml
import snow.GithubAccess.Config.githubUsername
import snow.GithubAccess.e.config
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class GithubAccess : PluginBase() {
    object e {
        val config = File("Plug-in/GithubAccess/key.yml")
    }

    override fun onStart(jda: JDA) {
        val folder = File("Plug-in/GithubAccess")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val input = this::class.java.classLoader.getResourceAsStream("key.yml") ?: throw FileNotFoundException("key.yml not found in resources")
        if (!config.exists()) {
            config.outputStream().use { input.copyTo(it) }
        }

    }
    object Config {
        var githubUsername: String? = "username"
    }

    override fun Commands(): List<CommandData> {
        return listOf(
            Commands.slash("github-access", "Access github profile")
                .addOption(OptionType.STRING, githubUsername.toString(), "github profile username"),
        )
    }

    override fun EventListeners(): List<ListenerAdapter> {
        return listOf(
            object : ListenerAdapter() {
                override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
                    if (event.name == "github-access") {
                         githubUsername = event.getOption("username")?.asString

                        val client = OkHttpClient()
                        val yaml = Yaml()
                        val token = yaml.load(FileInputStream(config)) as Map<String, Any>
                            // d
                            var url = "https://api.github.com/users/${githubUsername}"
                        val request = Request.Builder()
                            .url(url)
                            .addHeader("Authorization", "token ${token["Github_API_Token"].toString()}")
                            .addHeader("Accept", "application/vnd.github.v3+json")
                            .build()

                        try{
                            var response = client.newCall(request).execute()
                            if (!response.isSuccessful) throw IOException("Unexpected code $response")
                            if (response.body == null) throw IOException("Body null error occurred")
                            event.reply(response.body.string()).queue()
                            response.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        )
    }
}