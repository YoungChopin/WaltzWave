package de.youngchopin;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter {
    public static boolean stalk = false;
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if(stalk)
            System.out.println(event.getMessage().getAuthor() + " Server: " + event.getGuild().getName() + ": " + event.getMessage().getContentStripped());

    }
}
