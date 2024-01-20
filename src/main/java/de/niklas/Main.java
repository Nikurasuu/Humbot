package de.niklas;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        Logger logger = LogManager.getLogger(Main.class);

        String token = dotenv.get("discord-bot-token");

        DiscordClient client = DiscordClient.create(token);
        GatewayDiscordClient gateway = client.login().block();

        gateway.on(ReadyEvent.class).subscribe(event -> {
            logger.info("Logged in as " + event.getSelf().getUsername());
        });

        gateway.on(MessageCreateEvent.class).subscribe(event -> {
            Message message = event.getMessage();
            if ("!ping".equals(message.getContent())) {
                MessageChannel channel = message.getChannel().block();
                channel.createMessage("Pong!").block();
            }
        });

        gateway.onDisconnect().block();
    }
}