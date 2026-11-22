package tests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Pet;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPet {

    private static final String BASE_URL = "https://petstore.swagger.rv-school.ru/api/v3";

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
                        .delete(BASE_URL + "/pet/9876"));

        String responseBody = response.getBody().asString();

        step("Проверить, что статус-код ответа == 200", () ->
                assertEquals(200, response.getStatusCode(),
                        "Код ответа не совпал с ожидаемым. Ответ: " + responseBody));

        step("Проверить, что текст ответа 'Pet deleted'", () ->
                assertEquals("Pet deleted", responseBody,
                        "Текст ответа не совпал с ожидаемым. Получен: " + responseBody));

    }

    @Test
    @Feature("Pet")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("roman shvets")
    public void testUpdateNonexistentPet() {

        Pet pet = new Pet();
        pet.setId(9876);
        pet.setName("Non-existent Pet");
        pet.setStatus("available");

        Response response = step("Отправить PUT запрос на обновление несуществующего питомца", () ->
                given()
                        .contentType(ContentType.JSON)
                        .header("Accept", "application/json")
                        .body(pet)
                        .when()
                        .put(BASE_URL + "/pet"));

        String responseBody = response.getBody().asString();

        // 3. Проверяем код и текст
        step("Проверить, что статус-код ответа == 404", () ->
                assertEquals(404, response.getStatusCode(),
                        "Код ответа не совпал с ожидаемым. Ответ: " + responseBody));

        step("Проверить, что текст ответа 'Pet not found'", () ->
                assertEquals("Pet not found", responseBody,
                        "Текст ответа не совпал с ожидаемым. Получен: " + responseBody));
    }

    @Test
    @Feature("Pet")
    @Severity(SeverityLevel.NORMAL)
    @Owner("roman shvets")
    public void testGetNonexistentPet() {

        Response response = step("Отправить GET-запрос на получение несуществующего питомца", () ->
                given()
                        .contentType(ContentType.JSON)
                        .header("Accept", "application/json")
                        .when()
                        .get(BASE_URL + "/pet/9876"));

        String responseBody = response.getBody().asString();

        step("Проверить, что статус-код ответа == 404", () ->
                assertEquals(404, response.getStatusCode(),
                        "Код ответа не совпал с ожидаемым. Ответ: " + responseBody));

        step("Проверить, что текст ответа 'Pet not found'", () ->
                assertEquals("Pet not found", responseBody,
                        "Текст ответа не совпал с ожидаемым. Получен: " + responseBody));
    }

    // ПАРАМЕТРИЗОВАННЫЙ ТЕСТ НА ДОБАВЛЕНИЕ ПИТОМЦА
    @ParameterizedTest(name = "Создание питомца со статусом: {2} (id/name = {0}/{1})")
    @CsvSource({
            "9000, Bobby, available, 200",
            "9001, Sharik, pending, 200",
            "9002, Alpha, sold, 200",
            "9003, Betta, unknown, 200",
    })
    @Feature("Pet")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("roman shvets")
    public void testAddNewPet(int id, String name, String status, int respCode) {

        Pet newPet = new Pet();
        newPet.setId(id);
        newPet.setName(name);
        newPet.setStatus(status);
        newPet.setPhotoUrls(List.of("URL01", "URL02"));

        Response response = step("Отправить POST-запрос на создание нового питомца", () ->
                given()
                        .contentType(ContentType.JSON)
                        .header("Accept", "application/json")
                        .body(newPet)
                        .when()
                        .post(BASE_URL + "/pet"));

        String responseBody = response.getBody().asString();

        step("Проверка, что статус-код ответа == " + respCode, () ->
                assertEquals(respCode, response.getStatusCode(),
                        "Код ответа не совпал с ожидаемым. Ответ: " + responseBody));

        step("Проверка параметров созданного питомца", () -> {
            Pet createdPet = response.as(Pet.class);
            assertEquals(newPet.getId(), createdPet.getId(),"id питомца не совпадает с ожидаемым");
            assertEquals(newPet.getName(), createdPet.getName(),"name питомца не совпадает с ожидаемым");
            assertEquals(newPet.getStatus(), createdPet.getStatus(),"status питомца не совпадает с ожидаемым");
        });

    }

    // Данные готовит отдельный статический метод - удобно, когда нужны объекты
    static Stream<Pet> petsToCreate() {
        Pet a = new Pet(); a.setId(1101); a.setName("Rex");  a.setStatus("available");
        Pet b = new Pet(); b.setId(1102); b.setName("Tom");  b.setStatus("sold");
        return Stream.of(a, b);
    }

    @ParameterizedTest(name = "Создание питомца {0}")
    @MethodSource("petsToCreate")
    public void testAddPetFromObject(Pet newPet) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(newPet)
                .when()
                .post(BASE_URL + "/pet");

        //assertEquals(pet.getName(), response.as(Pet.class).getName());

        String responseBody = response.getBody().asString();

        step("Проверка, что статус-код ответа == 200", () ->
                assertEquals(200, response.getStatusCode(),
                        "Код ответа не совпал с ожидаемым. Ответ: " + responseBody));

        step("Проверка параметров созданного питомца", () -> {
            Pet createdPet = response.as(Pet.class);
            assertEquals(newPet.getId(), createdPet.getId(),"id питомца не совпадает с ожидаемым");
            assertEquals(newPet.getName(), createdPet.getName(),"name питомца не совпадает с ожидаемым");
            assertEquals(newPet.getStatus(), createdPet.getStatus(),"status питомца не совпадает с ожидаемым");
        });
    }
}
