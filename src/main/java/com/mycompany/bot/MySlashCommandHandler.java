package com.mycompany.bot;

import com.symphony.bdk.core.activity.command.CommandContext;
import com.symphony.bdk.core.service.message.MessageService;
import com.symphony.bdk.core.service.message.model.Message;
import com.symphony.bdk.spring.annotation.Slash;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * This class shows how a slash command is created/handled
 */
@Component
@Slf4j
public class MySlashCommandHandler {
    /**
     * This allows us to communicate with bot, and send message back to bot.
     */
    private MessageService messageService;

    public MySlashCommandHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    @Slash(value = "/my-top-stock", mentionBot = false)
    public void onSlashStockPrice(CommandContext context) {
        String command = context.getTextContent();
        command = command.replaceAll("<[^>]*>", "");
        log.info("Received command : " + command);

        String stock = "$TSLA";
        int price = (new Random()).nextInt(100) + 100;
        String message = stock + " : $" + price;
        this.messageService.send(context.getStreamId(), message);
    }

    /**
     * This command shows how we can receive attachments/documents from a user and
     * send an attachment/document back to a user
     * @param context
     */
    @Slash(value="/update-portfolio", mentionBot = false)
    public void onSlashBulkStockPrice(CommandContext context) {
        byte[] attachment = messageService.getAttachment(context.getStreamId(),
            context.getMessageId(), context.getSourceEvent().getMessage().getAttachments()
                                        .get(0).getId());
        String base64 = new String(attachment);
        String content = new String(Base64.getDecoder().decode(base64));
        String messageText = context.getSourceEvent().getMessage().getMessage().replaceAll("<[^>]*>", "");
        log.info("Attachment data received : " + messageText);

        // bot will process this data now

        // after processing return some response back to the user
        Message msg = Message.builder().content("Your portfolio has been updated, please see attached document")
                          .addAttachment(
                              new ByteArrayInputStream(
                                  Charset.forName("UTF-16")
                                      .encode(content).array()), "YourPortfolio.txt")
                          .build();

        this.messageService.send(context.getSourceEvent().getMessage().getStream(), msg);
    }
}
