package snow

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class Tickets: PluginBase() {
    override fun onStart(jda: JDA) {
    }

    override fun Commands(): List<CommandData> {
        return listOf(
            Commands.slash("tickets", "Tickets Button"),
        )
    }

    override fun EventListeners(): List<ListenerAdapter> {
        return listOf(
            object: ListenerAdapter() {
                override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
                    if (event.name == "tickets") {
                        if (event.user.idLong != 705674606043856956) {
                            event.reply("you cannot use tickets!")
                            return
                        }
                        event.replyEmbeds(EmbedBuilder()
                            .setTitle("Ticket office")
                            .setDescription("如有問題請點擊以下按鈕 \nIf you have any problem, please click the button below \nご質問がある場合は、下のボタンをクリックしてください。")
                            .build())
                            .addActionRow(Button.success("Ticket", ":envelope_with_arrow: ")).queue()
                    }
                }
            }, ButtonClick()
        )
    }

    class ButtonClick: ListenerAdapter() {
        override fun onButtonInteraction(event: ButtonInteractionEvent) {
            if (event.componentId == "Ticket") {
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
                        .setDescription("請發出你的問題 \nPlease send the problem in the channel \nご質問をお送りください")
                        .setFooter(event.jda.selfUser.name).build()
                    ).queue { channel.sendMessage("Sender:" + event.user.asMention).queue()}}
            }
        }
    }

}