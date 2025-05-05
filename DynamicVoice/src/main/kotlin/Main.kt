package snow

import jdk.internal.org.jline.utils.Colors.s
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.yaml.snakeyaml.Yaml
import snow.DynamicVoice.e.config
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.nio.channels.Channel

class DynamicVoice: PluginBase() {
    object e {
        val config = File("Plug-in/DynamicVoice/_config.yml")
    }

    override fun onStart(jda: JDA) {

        val folder = File("Plug-in/DynamicVoice")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val input = this::class.java.classLoader.getResourceAsStream("_config.yml") ?: throw FileNotFoundException("_config.yml not found in resources")
        if (!config.exists()) {
            config.outputStream().use { input.copyTo(it) }
        }
    }

    override fun EventListeners(): List<ListenerAdapter> {
        return listOf(
            object : ListenerAdapter() {
                override fun onGuildVoiceUpdate(event: GuildVoiceUpdateEvent) {
                    try {
                    val yaml = Yaml()
                    val token = yaml.load(FileInputStream(config)) as Map<String, Any>
                    val Channel_ID = token["channel"] as? List<Any> ?: emptyList()
                    for (channel in Channel_ID) {

                        if (event.channelJoined?.idLong == channel) {
                            val name = event.member.user.name
                            val s = token["channel_category"] as Long
                            event.guild.getCategoryById(s)?.createVoiceChannel("${name}|啥也不是")
                                ?.queue { channel ->
                                    event.guild.moveVoiceMember(event.member, channel).queue()
                                }
                        } else {
                            val s = token["channel_category"] as Long
                            val voicechannel: VoiceChannel? = event.guild.getVoiceChannelById(event.channelLeft?.idLong!!)
                            if (voicechannel?.parentCategory?.idLong == s) {
                                if (voicechannel.members.size == 0) {
                                    voicechannel.delete().queue()
                                }
                            }
                        }
                    }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }


            }
        )
    }
}