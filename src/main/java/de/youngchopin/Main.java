package de.youngchopin;


import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Main{
        public static void main(String[] args) {
                ConsoleGUI consoleGUI = new ConsoleGUI();
                consoleGUI.setVisible(true);
                String token = TOKEN;
                DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
                builder.setEnabledIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.SCHEDULED_EVENTS, GatewayIntent.GUILD_MESSAGES);
                builder.enableCache(CacheFlag.VOICE_STATE);
                builder.addEventListeners(new CommandManager());
                builder.addEventListeners(new Listener());
                builder.build();
            }
        }



