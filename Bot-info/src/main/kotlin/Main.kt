package snow

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.OnlineStatus.ONLINE
import net.dv8tion.jda.api.entities.Activity

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class BotInfo: PluginBase() {
    override fun onStart(jda: JDA) {

        jda.presence.activity = Activity.playing("huh?")
        jda.awaitReady()
    }
}