package pro.entera.message_sender.services;

import pro.entera.message_sender.dtos.MessageSenderTaskDTO;

/**
 * Интерфейс сервиса отправки уведомлений.
 */
public interface MessageSenderService {
    //region Public

    /**
     * Формирует и отправляет сообщение по указанной задачи на уведомление.
     *
     * @param task Задача на уведомление.
     */
    void sendMessage(MessageSenderTaskDTO task);

    //endregion
}
