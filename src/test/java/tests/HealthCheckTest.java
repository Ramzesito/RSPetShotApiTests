package test;

import core.clients.APIClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HealthCheckTest {
    private APIClient apiClient;

    @BeforeEach                      // выполняется перед КАЖДЫМ тестом класса
    public void setUp() {
        apiClient = new APIClient(); // свежий клиент для каждого теста
    }

    @Test
    public void pingShouldReturn201() {
        Response response = apiClient.ping();

        // Вариант 1 - как в уроке: проверяем булево выражение
        //assertThat(response.getStatusCode() == 201).isTrue();
        // При падении: Expecting value to be true but was false  (какой код пришёл - не видно)

        // Вариант 2 - лучше: сравниваем числа
        assertThat(response.getStatusCode()).isEqualTo(201);
        // При падении: expected: 201  but was: 500  (сразу видно причину)

        // Вариант 3 - ещё несколько полезных проверок AssertJ
        assertThat(response.getTime()).isLessThan(3000L);          // ответ быстрее 3 секунд
        assertThat(response.getContentType()).contains("text");    // тип содержимого
    }
}
// Вывод при запуске: зелёная галочка, 1 test passed