package pro.entera.message_sender.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pro.entera.message_sender.dtos.MessageSenderTaskDTO;
import pro.entera.message_sender.services.MessageSenderService;

/**
 * Компонент для обработки входящих сообщений из очереди RabbitMQ.
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class RabbitMQMessageReceiver {
    //region Fields

    /**
     * Сервис для обработки задач отправки сообщений.
     */
    private final MessageSenderService messageSenderService;

    //endregion
    //region Public

    /**
     * Метод для обработки входящих задач из очереди RabbitMQ.
     *
     * @param task объект задачи {@link MessageSenderTaskDTO}, полученный из очереди.
     */
    @RabbitListener(queues = RabbitMqConfig.PROCESS_QUEUE_NAME)
    public void receiveTask(MessageSenderTaskDTO task) {

        try {

            this.messageSenderService.sendMessage(task);
        }
        catch (Exception e) {

            RabbitMQMessageReceiver.log.error("Error while processing task", e);
        }
    }

    //endregion
}
