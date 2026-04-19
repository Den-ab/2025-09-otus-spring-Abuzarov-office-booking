package pro.entera.message_sender.providers;

/**
 * Функция для получения сервиса интернационализации внутри шаблонов.
 */
@FunctionalInterface
public interface MessageProvider {
    //region Public

    String getMessage(String key, Object... args);

    //endregion
}
