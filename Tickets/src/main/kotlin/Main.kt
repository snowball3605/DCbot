package snow

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageHistory
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.utils.FileUpload
import java.io.ByteArrayInputStream
import java.time.format.DateTimeFormatter

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class Tickets: PluginBase() {
    override fun onStart(jda: JDA) {
    }

    override fun Commands(): List<CommandData> {
        return listOf(
            Commands.slash("tickets_tw", "Tickets Button"),
            Commands.slash("tickets_en", "Tickets"),
        )
    }

    override fun EventListeners(): List<ListenerAdapter> {
        return listOf(
            object: ListenerAdapter() {
                override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
                    if (event.name == "tickets_tw") {
                        if (event.user.idLong != 705674606043856956) {
                            event.reply("you cannot use tickets!")
                            return
                        }
                        event.replyEmbeds(EmbedBuilder()
                            .setTitle("Ticket office")
                            .setDescription("如有問題請點擊以下按鈕")
                            .build())
                            .addActionRow(Button.success("Ticket_tw", ":envelope_with_arrow:")).queue()
                    } else if (event.name == "tickets_en") {
                        if (event.user.idLong != 705674606043856956) {
                            event.reply("you cannot use tickets!")
                            return
                        }
                        event.replyEmbeds(EmbedBuilder()
                            .setTitle("Ticket office")
                            .setDescription("If you have any problem, please click the button below")
                            .build())
                            .addActionRow(Button.success("Ticket_en", ":envelope_with_arrow:")).queue()
                    }
                }
            }, ButtonClick()
        )
    }

    class ButtonClick: ListenerAdapter() {
        override fun onButtonInteraction(event: ButtonInteractionEvent) {
            if (event.componentId == "Ticket_tw") {
                val guild = event.guild ?: throw Exception("No guild selected")
                val cat = guild.getCategoryById("1344196319178260508") ?: throw Exception("No Category selected")

                cat.createTextChannel("Ticket-${event.user.name}")
                    .addPermissionOverride(
                        guild.publicRole,
                        null,
                        listOf(Permission.VIEW_CHANNEL)
                    )
                    .addPermissionOverride(
                        guild.getMember(event.user)!!,
                        listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                        null
                    ).queue { channel -> channel.sendMessageEmbeds(EmbedBuilder()
                        .setTitle("Problem opening Ticket")
                        .setDescription("請發出你的問題")
                        .setFooter(event.jda.selfUser.name)
                        .build()
                    ).addActionRow(Button.success("Off_Ticket", "關閉客服單")).queue { channel.sendMessage("發送者: " + event.user.asMention + "  有權限的人:" + event.guild?.getRoleById(1314107700287897630)?.asMention).queue()}}
            } else if (event.componentId == "Ticket_en") {
                val guild = event.guild ?: throw Exception("No guild selected")
                val cat = guild.getCategoryById("1344196319178260508") ?: throw Exception("No Category selected")

                cat.createTextChannel("Ticket-${event.user.name}")
                    .addPermissionOverride(
                        guild.publicRole,
                        null,
                        listOf(Permission.VIEW_CHANNEL)
                    )
                    .addPermissionOverride(
                        guild.getMember(event.user)!!,
                        listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                        null
                    ).queue { channel -> channel.sendMessageEmbeds(EmbedBuilder()
                        .setTitle("Problem opening Ticket")
                        .setDescription("Please send your question")
                        .setFooter(event.jda.selfUser.name)
                        .build()
                    ).addActionRow(Button.success("Off_Ticket", "Close the ticket")).queue { channel.sendMessage("Sender: " + event.user.asMention + "  Admin:" + event.guild?.getRoleById(1314107700287897630)?.asMention).queue()}}
            }

            else if (event.componentId == "Off_Ticket") {
                if (event.member?.hasPermission(Permission.ADMINISTRATOR) == true) {
                    event.channel.delete();
                } else {
                    event.reply("You cannot close this ticket!")
                }
            }
        }

    }

}