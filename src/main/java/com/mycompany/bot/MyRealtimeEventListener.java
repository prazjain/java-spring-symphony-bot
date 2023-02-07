package com.mycompany.bot;

import com.symphony.bdk.core.service.datafeed.EventException;
import com.symphony.bdk.core.service.message.MessageService;
import com.symphony.bdk.gen.api.model.V4Message;
import com.symphony.bdk.gen.api.model.V4MessageSent;
import com.symphony.bdk.spring.events.RealTimeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyRealtimeEventListener {

    /**
     * This allows us to communicate with bot, and send message back to bot.
     */
    private final MessageService messageService;

    @Autowired
    public MyRealtimeEventListener(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * This method is called by BDK, when a new message is received.
     * We clean the message of any formatting tags, and process it based on its contents.
     * In the case of this method, we process a message only if it is 'hi', else we ignore it.
     * @param event
     * @throws EventException
     */
    @EventListener
    public void onMessage(RealTimeEvent<V4MessageSent> event)
    throws EventException {
        V4Message v4Message = event.getSource().getMessage();
        String message = v4Message.getMessage().replaceAll("<[^>]*>", "");
        if (message.startsWith("hi")) {
            log.info("Chat message received " + message);
            messageService.send(v4Message.getStream(), "Hi there");
            log.info("Responded back to the user");
        }
    }
}
