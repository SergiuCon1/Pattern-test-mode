package ru.netology;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthTest {

    DataGenerator data = new DataGenerator();

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=\"login\"] .input__control").val(registeredUser.getLogin());
        $("[data-test-id=\"password\"] .input__control").val(registeredUser.getPassword());
        $("[data-test-id=\"action-login\"] .button__content").click();
        String text = $("[id=\"root\"] .App_appContainer__3jRx1").getText();
        assertEquals("  Личный кабинет", text);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=\"login\"] .input__control").val(notRegisteredUser.getLogin());
        $("[data-test-id=\"password\"] .input__control").val(notRegisteredUser.getPassword());
        $("[data-test-id=\"action-login\"] .button__content").click();
        $("[data-test-id='error-notification'] .notification__content").should(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=\"login\"] .input__control").val(blockedUser.getLogin());
        $("[data-test-id=\"password\"] .input__control").val(blockedUser.getPassword());
        $("[data-test-id=\"action-login\"] .button__content").click();
        $("[data-test-id='error-notification'] .notification__content").should(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = data.getRandomLogin();
        $("[data-test-id=\"login\"] .input__control").val(wrongLogin);
        $("[data-test-id=\"password\"] .input__control").val(registeredUser.getPassword());
        $("[data-test-id=\"action-login\"] .button__content").click();
        $("[data-test-id='error-notification'] .notification__content").should(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = data.getRandomPassword();
        $("[data-test-id=\"login\"] .input__control").val(registeredUser.getLogin());
        $("[data-test-id=\"password\"] .input__control").val(wrongPassword);
        $("[data-test-id=\"action-login\"] .button__content").click();
        $("[data-test-id='error-notification'] .notification__content").should(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }
}
