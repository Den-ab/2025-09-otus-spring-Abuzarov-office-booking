package pro.entera.message_sender.services.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.entera.message_sender.config.email.EmailConfig;
import pro.entera.message_sender.dtos.MessageSenderTaskDTO;
import pro.entera.message_sender.models.MessageTemplateType;
import pro.entera.message_sender.services.MessageSenderService;
import pro.entera.message_sender.services.i18n.I18nService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Сервис для отправки уведомлений на электронную почту.
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "message-sender")
public class EmailMessageSenderService implements MessageSenderService {
    //region

    /**
     * Сервис сборки шаблонов для отправки уведомлений на электронную почту.
     */
    private final EmailTemplateService emailTemplateService;

    /**
     * Конфиг электронной почты.
     */
    private final EmailConfig emailTemplateConfig;

    /**
     * Сервис отправки сообщения на электронную почту.
     */
    private final EmailSenderService emailSenderService;

    /**
     * Сервис интернационализации.
     */
    private final I18nService i18nService;

    //endregion
    //region Public

    @Override
    public void sendMessage(MessageSenderTaskDTO task) {

        final Map<String, Object> fields = Optional.ofNullable(task.getMessageFields()).orElseGet(HashMap::new);
        String res = this.emailTemplateService.generateEmailTemplate(
            fields,
            emailTemplateConfig.getEmailTemplatesPath(),
            MessageTemplateType.valueOf(task.getMessageTemplateType()),
            i18nService
        );
        var subject = task.getMessageFields().get("subject").toString();
        EmailMessageSenderService.log.debug("Send message to " + task.getMessageAddress());
        this.emailSenderService.sendMessage(task.getMessageAddress(), subject, res, task.getEmailSender());
        EmailMessageSenderService.log.debug("Message sent to " + task.getMessageAddress());
    }

    //endregion
}
