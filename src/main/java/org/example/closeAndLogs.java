package org.example;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class closeAndLogs extends ListenerAdapter {

     FileWriter log = null;
    public void onMessageReceived(MessageReceivedEvent e){
        User author = e.getAuthor();
        if(author.isBot()) return;
        try{
            if(author.equals(e.getGuild().getSelfMember().getUser())) return;
        }catch(IllegalStateException exception){}

        String args[] = e.getMessage().getContentRaw().split(" ");
        if(args[0].equalsIgnoreCase("?close")){
            try{
                if(e.getTextChannel().getTopic().equalsIgnoreCase("Kai Support Modmail")){
                   try{
                       e.getChannel().sendMessage("**Closing modmail....**").queue();
                   }catch (Exception exception){}

                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }






                    StringBuilder reply = new StringBuilder();
                    for(int i = 1; i < args.length ; i++){
                        reply.append(args[i] + " ");
                    }

                    //logs
                    String Time = e.getMessage().getTimeCreated().toString().replace('T', ' ').replace('Z', ' ').split(" ")[1];
                    String date = e.getMessage().getTimeCreated().toString().replace('T', ' ').replace('Z', ' ').split(" ")[0];

                    StringBuilder embedUrls = new StringBuilder();
                    e.getMessage().getEmbeds().forEach(messageEmbed -> embedUrls.append(messageEmbed.getUrl() + " "));

                    log = new FileWriter(String.format("%s.txt", e.getTextChannel().getName()), true);
                    log.append("\n");
                    if(args.length != 1){
                        log.append(String.format("Moderator: %s closed the channel with the reason: %s (Time: %s(UTC) , Date: %s)", author.getName(), reply, Time, date));
                    }else{
                        log.append(String.format("Moderator: %s closed the ticket (Time: %s(UTC) , Date: %s)", author.getName(), Time, date));

                    }
                    log.close();


                    //deleting the log
                    String path = String.format("%s.txt", e.getTextChannel().getName());
                    Path xpath = Paths.get(path);
                    System.out.println(xpath);

                    File file = new File(path);
                    e.getGuild().getTextChannelById("980322609822576640").sendFile(file)
                            .queue();

                    Thread.sleep(250);

                    try{
                        Files.delete(xpath);

                    }catch (Exception exception){
                        System.out.println("Unable to delete file");
                        System.err.println(exception.getStackTrace());
                    }




                    if(args.length != 1) {
                        e.getGuild().retrieveMemberById(e.getTextChannel().getName().split("-")[e.getTextChannel().getName().split(" ").length + 1])
                                .complete().getUser().openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(String.format("**Support ticket closed with the reason: %s .**", reply))).queue();
                    }else{
                        e.getGuild().retrieveMemberById(e.getTextChannel().getName().split("-")[e.getTextChannel().getName().split(" ").length + 1])
                                .complete().getUser().openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage("**Support ticket closed**")).queue();
                    }

                  e.getChannel().delete().queue();

                    //here should be the logging system
                }else{
                    e.getChannel().sendMessage("**this is not a modmail thread**").queue();
                }
            }catch(Exception exception){
                //this will also catch if it's a modmail thread incase there is no topics
            }
        }
    }
}
