package pro.entera.message_sender.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import pro.entera.message_sender.config.email.EmailConfig;

import java.util.Locale;

/**
 * Конфигурация приложения.
 */
@ConfigurationProperties(prefix = "application")
@Getter
public class AppProperties implements EmailConfig {
    //region Fields

    /**
     * Путь до темплейтов для email.
     */
    private final String emailTemplatesPath;

    /**
     * Российский адрес отправителя электронной почты.
     */
    private final String emailSenderAddress;

    /**
     * Путь до логотипа.
     */
    private final String enteraLogoPath;

    /**
     * Локаль приложения.
     */
    private final Locale locale;

    //endregion
    //region Ctor

    /**
     * Конструктор пропертей.
     *
     * @param emailTemplatesPath Путь до темплейтов для email.
     * @param emailSenderAddress Российский адрес отправителя электронной почты.
     * @param enteraLogoPath Путь до логотипа.
     * @param locale Локаль приложения
     */
    public AppProperties(String emailTemplatesPath, String emailSenderAddress, String enteraLogoPath, String locale) {

        this.emailTemplatesPath = emailTemplatesPath;
        this.emailSenderAddress = emailSenderAddress;
        this.enteraLogoPath = enteraLogoPath;
        this.locale = Locale.forLanguageTag(locale);
    }

    //endregion
}
