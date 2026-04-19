package pro.entera.message_sender.config.email;

import java.util.Locale;

/**
 * Интерфейс общих настроек для формирования писем.
 */
public interface SenderConfig {
    //region Public

    /**
     * Возвращает путь до файла логотипа.
     *
     * @return Путь до логитипа.
     */
    String getEnteraLogoPath();

    /**
     * Возвращает локаль.
     *
     * @return Локаль.
     */
    Locale getLocale();

    //endregion
}
