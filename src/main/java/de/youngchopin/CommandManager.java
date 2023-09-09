package de.youngchopin;

import de.youngchopin.lavaPlayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("join", "joins the talk"));
        OptionData songURL = new OptionData(OptionType.STRING, "name", "name of the song", true);
        commandData.add(Commands.slash("play", "plays Musik").addOptions(songURL));
        commandData.add(Commands.slash("stop", "stops die Musik"));
        commandData.add(Commands.slash("skip", "skips the current"));
        commandData.add(Commands.slash("ping", "tells you the latency"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        switch (command) {
            case "join" -> {
                Guild guild = event.getGuild();
                Member member = event.getMember();
                if(member.getVoiceState().inAudioChannel()) {
                    VoiceChannel voiceChannel = member.getVoiceState().getChannel().asVoiceChannel();
                    AudioManager audioManager = guild.getAudioManager();

                    audioManager.openAudioConnection(voiceChannel);
                    event.reply("There i am").queue();
                }
                else {
                    event.reply("Join a Voice Channel first!").queue();
                }
            }
            case "ping" -> event.reply("The ping is " + event.getJDA().getGatewayPing());
            case "play" -> {
                String link = event.getOption("url").getAsString();
                if (!event.getMember().getVoiceState().inAudioChannel()) {
                    event.reply("Du musst in einem Voice Channel sein").setEphemeral(true).queue();
                    return;
                }
                if (!event.getGuild().getSelfMember().getVoiceState().inAudioChannel()) {
                    final AudioManager audioManager = event.getGuild().getAudioManager();
                    final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

                    audioManager.openAudioConnection(memberChannel);
                }
                if (!isURL(link)) {
                    link = "ytsearch:" + link + " audio";
                }
                event.reply("Musik wird gesucht...").queue();
                PlayerManager.getInstance().loadAndPlay(event.getChannel().asTextChannel(), link);
            }
            case "stop" -> {
                final AudioManager audioManager = event.getGuild().getAudioManager();
                audioManager.closeAudioConnection();
                event.reply("Musik wurde gestoppt").queue();
            }
            case "skip" -> {
                PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.nextTrack();
                event.reply("Musik wurde übersprungen").queue();
            }
        }
    }

    public boolean isURL(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

}
