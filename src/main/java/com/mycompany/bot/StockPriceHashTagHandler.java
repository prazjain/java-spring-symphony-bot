package com.mycompany.bot;

import static java.util.Collections.singletonMap;

import com.symphony.bdk.core.activity.ActivityMatcher;
import com.symphony.bdk.core.activity.command.CommandActivity;
import com.symphony.bdk.core.activity.command.CommandContext;
import com.symphony.bdk.core.activity.model.ActivityInfo;
import com.symphony.bdk.core.activity.model.ActivityType;
import com.symphony.bdk.core.service.datafeed.EventException;
import com.symphony.bdk.core.service.message.MessageService;
import com.symphony.bdk.core.service.message.model.Message;
import com.symphony.bdk.template.api.Template;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * In this command handler, we see how to handle a custom command
 */
@Component
@Slf4j
public class StockPriceHashTagHandler
    extends CommandActivity<CommandContext> {

    private final Template template;
    List<String> data = Arrays.asList("TSLA - $200",
        "$GOOGL - $100",
        "$AMZN - $110",
        "$APPL - $130",
        "$FB - $180"
        );
    /**
     * This allows us to communicate with bot, and send message back to bot.
     */
    private MessageService messageService;

    public StockPriceHashTagHandler(MessageService messageService) {
        this.messageService = messageService;
        this.template = messageService.templates()
                            .newTemplateFromClasspath
                                ("/templates/form_default_msg_with_feedback.ftl");
    }

    @Override
    protected ActivityMatcher<CommandContext> matcher() throws EventException {
        // handle messages that contain a #stockoftheday
        return c -> c.getTextContent().contains("#stockoftheday");
    }

    @Override
    protected void onActivity(CommandContext commandContext) throws EventException {
        int randomIndex = (new Random()).nextInt(5) ;
        String message = data.get(randomIndex);
        messageService.send(commandContext.getStreamId(), Message.builder()
                 .template(template, singletonMap("message", message)).build());
    }

    @Override
    protected ActivityInfo info() {
        return new ActivityInfo().type(ActivityType.COMMAND)
                   .name("Stock price command");
    }
}
