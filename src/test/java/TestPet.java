import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPet {

    private static final String BASE_URL = "http://5.181.109.28:9090/api/v3";

    @Test
    @Feature("Pet")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("roman shvets")
    @Description("Это описание теста: Удаление несуществующего животного")
    @Tag("pet")
    public void testDeleteNonExistentPet() {

        Response response = step("Отправить DELETE-запрос на удаление несуществующего питомца", () ->
                given()
                        .contentType(ContentType.JSON)
                        .header("Accept", "application/json")
                        .when()
                        .delete(BASE_URL + "/pet/999"));

        String responseBody = response.getBody().asString();

        step("Проверить, что статус-код ответа == 200", () ->
                assertEquals(200, response.getStatusCode(),
                        "Код ответа не совпал с ожидаемым. Ответ: " + responseBody));

        step("Проверить, что текст ответа 'Pet deleted'", () ->
                assertEquals("Pet deleted", responseBody,
                        "Текст ответа не совпал с ожидаемым. Получен: " + responseBody));

    }
}
