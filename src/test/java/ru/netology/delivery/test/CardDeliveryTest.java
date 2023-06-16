package ru.netology.delivery.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id = city] input").setValue(validUser.getCity());
        $("[data-test-id = date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id = date] input").setValue(firstMeetingDate);
        $("[data-test-id = name] input").setValue(validUser.getName());
        $("[data-test-id = phone] input").setValue(validUser.getPhone());
        $("[data-test-id = agreement]").click();
        $(byText("Запланировать")).click();
        $x("//*[contains(text(),'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id = date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id = date] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();
        $x("//*[contains(text(),'Необходимо подтверждение')]").shouldBe(visible);
        $x("//*[contains(text(),'У вас уже запланирована встреча на другую дату. Перепланировать?')]").shouldBe(visible);
        $(byText("Перепланировать")).click();
        $x("//*[contains(text(),'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate)).shouldBe(visible);
    }
}