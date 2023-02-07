package com.mycompany.bot;

import com.symphony.bdk.core.activity.ActivityMatcher;
import com.symphony.bdk.core.activity.form.FormReplyActivity;
import com.symphony.bdk.core.activity.form.FormReplyContext;
import com.symphony.bdk.core.activity.model.ActivityInfo;
import com.symphony.bdk.core.activity.model.ActivityType;
import com.symphony.bdk.core.service.datafeed.EventException;
import com.symphony.bdk.core.service.message.MessageService;
import com.symphony.bdk.core.service.message.model.Message;
import org.springframework.stereotype.Component;

@Component
public class FeedbackFormActivity extends FormReplyActivity<FormReplyContext> {

    private MessageService messageService;

    public FeedbackFormActivity(MessageService messageService) {

        this.messageService = messageService;
    }

    @Override
    protected ActivityMatcher<FormReplyContext> matcher() throws EventException {
        return context -> "form_default_msg_with_feedback".equals(context.getFormId())
                              && "btnSubmit".equals(context.getFormValue("action"));
    }

    @Override
    protected void onActivity(FormReplyContext context) throws EventException {
        final String category = context.getFormValue("feedback");
        final String message = "<messageML>Thanks for your feedback : '" + category + "'</messageML>";
        this.messageService.send(context.getSourceEvent().getStream(), Message.builder().content(message).build());
    }

    @Override
    protected ActivityInfo info() {
        return new ActivityInfo().type(ActivityType.FORM)
                   .name("Feedback form command")
                   .description("\"Form handler for the Feedback form\"");
    }
}
