package snow

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class Tickets: PluginBase() {
    override fun onStart(jda: JDA) {
        jda.updateCommands().addCommands(Commands.slash("tickets", "Tickets Button")).queue()

        jda.addEventListener(object: ListenerAdapter() {
            override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
                if (event.name == "Tickets") {
                    event.replyEmbeds(EmbedBuilder()
                        .setTitle("Ticket office")
                        .setDescription("如有問題請點擊以下按鈕 \nIf you have any problem, please click the button below \nご質問がある場合は、下のボタンをクリックしてください。")
                        .build())
                        .addActionRow(Button.success("Ticket", ":envelope_with_arrow: ")).queue()
                }
            }
        }, ButtonClick())
    }

    class ButtonClick: ListenerAdapter() {
        override fun onButtonInteraction(event: ButtonInteractionEvent) {
            if (event.componentId == "Ticket") {
                val guild = event.guild ?: return
                val cat = guild.getCategoryById("1344196319178260508") ?: return

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
                    ).queue()
            }
        }
    }

}