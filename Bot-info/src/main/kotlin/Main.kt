package snow

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.OnlineStatus.ONLINE
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.Commands

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class BotInfo: PluginBase() {
    override fun onStart(jda: JDA) {

        jda.presence.activity = Activity.playing("huh?")
        jda.awaitReady()

        jda.updateCommands().addCommands(
            Commands.slash("botinfo", "Bot Info")
        ).queue()

        jda.addEventListener(object : ListenerAdapter() {
            override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
                if (event.name == "botinfo") {
                    event.replyEmbeds(EmbedBuilder()
                        .setTitle("OmniTech Bot")
                        .setDescription("Name: OmniTech \nVersion: 1.0.0")
                        .setFooter("OmniTech Bot", event.jda.selfUser.avatarUrl)
                        .build()).queue()
                }
            }
        })
    }
}