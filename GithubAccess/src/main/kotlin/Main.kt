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
import snow.GithubAccess.Config.githubUsername
import java.io.IOException

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class GithubAccess : PluginBase() {
    override fun onStart(jda: JDA) {
        jda.awaitReady()

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
                         githubUsername = event.getOption(githubUsername.toString())?.asString
                        event.guild?.upsertCommand("github-access", "Access github repositories")
                            ?.addOption(OptionType.STRING, githubUsername.toString(), "github user name", true)
                            ?.queue()

                        val client = OkHttpClient()
                        val token = "Are you stupid? Why are you watch my token"

                        var url = "https://api.github.com/users/${githubUsername}"
                        val request = Request.Builder()
                            .url(url)
                            .addHeader("Authorization", "token $token")
                            .addHeader("Accept", "application/vnd.github.v3+json")
                            .build()

                        try{
                            val response = client.newCall(request).execute()
                            if (!response.isSuccessful) throw IOException("Unexpected code $response")
                            if (response.body == null) throw IOException("Body null error occurred")
                            event.reply(response.body.string()).queue()
                            println(url + "\n" + response.body.string())
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        )
    }
}