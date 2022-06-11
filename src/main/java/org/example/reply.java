package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class reply extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent e) throws IllegalStateException{
        User author = e.getAuthor();
        if(author.isBot()) return;
        if(author.equals(e.getGuild().getSelfMember().getUser())) return;
        if(e.getTextChannel().getTopic().equalsIgnoreCase("Kai Support Modmail")){
            if(e.getGuild().retrieveMember(author).complete().hasPermission(Permission.MODERATE_MEMBERS)){
                String args[] = e.getMessage().getContentRaw().split(" ");
                if(args[0].equalsIgnoreCase("?r")){
                    //messaging the user
                    StringBuilder reply = new StringBuilder();
                    for(int i = 1; i < args.length ; i++){
                        reply.append(args[i] + " ");
                    }
                    EmbedBuilder replyEmbed = new EmbedBuilder();
                    replyEmbed.setAuthor("Anonymous#0000", "https://www.youtube.com/watch?v=dQw4w9WgXcQ", "https://cdn.discordapp.com/avatars/907606359405633536/435ba02fd58a45d54fb0d66716c09cab.webp?size=2048");
                    replyEmbed.setDescription(reply.toString());

                    e.getGuild().retrieveMemberById(e.getTextChannel().getName().split("-")[e.getTextChannel().getName().split(" ").length + 1])
                            .complete().getUser().openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessageEmbeds(replyEmbed.build())).queue();

                    //sending embed to the channel

                    e.getMessage().delete().queue();
                    EmbedBuilder replyChannelEmbed = new EmbedBuilder();
                    replyChannelEmbed.setAuthor(author.getName() + "#" + author.getDiscriminator(), "https://www.youtube.com/watch?v=dQw4w9WgXcQ", author.getAvatarUrl());
                    replyChannelEmbed.setDescription(reply.toString());

                    e.getChannel().sendMessageEmbeds(replyChannelEmbed.build()).queue();
                }
            }
        }else{
            e.getChannel().sendMessage("**Not a modmail thread**").queue();
        }

    }
}
