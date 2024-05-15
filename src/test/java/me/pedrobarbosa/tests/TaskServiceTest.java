package me.pedrobarbosa.tests;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import jakarta.transaction.Transactional;
import me.pedrobarbosa.dto.DepartmentDTO;
import me.pedrobarbosa.dto.TaskDTO;
import me.pedrobarbosa.dto.UserDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.util.Collections;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskServiceTest implements ServiceTest {

    private static final String TASK_NAME = "Tarefa Teste", UPDATED_TASK_NAME = "Tarefa Teste Atualizada";

    @Override
    @Test
    @Order(1)
    @Transactional
    public void testCreate() {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(new DepartmentDTO(null, "TestDepartment", 0, 0))
                .post("/departamentos")
                .then()
                .statusCode(200);
        TaskDTO taskDTO = new TaskDTO(null, TASK_NAME, "Test desc", "01/01/2024", "02/01/2024", 24L, 1L, null, false);
        ValidatableResponse response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(taskDTO)
                .post("/tarefas")
                .then()
                .statusCode(200);
        JsonPath path = response.extract().body().jsonPath();
        Assertions.assertEquals(path.getLong("id"), 1L);
        Assertions.assertEquals(path.getString("title"), TASK_NAME);
        Assertions.assertEquals(path.getString("desc"), "Test desc");
        Assertions.assertEquals(path.getString("startDate"), "01/01/2024");
        Assertions.assertEquals(path.getString("endDate"), "02/01/2024");
        Assertions.assertEquals(path.getLong("duration"), 24L);
        Assertions.assertEquals(path.getLong("departmentId"), 1L);
        Assertions.assertNull(path.get("userId"));
        Assertions.assertFalse(path.getBoolean("terminated"));
    }

    @Override
    @Test
    @Order(2)
    @Transactional
    public void testCreateFail() {
        TaskDTO taskDTO = new TaskDTO(null, null, null, null, null, null, null, null, null);
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(taskDTO)
                .post("/tarefas")
                .then()
                .statusCode(500);
    }

    @Override
    @Test
    @Order(3)
    @Transactional
    public void testCreateExistent() {
        TaskDTO taskDTO = new TaskDTO(1L, TASK_NAME, "Test desc", "01/01/2024", "02/01/2024", 24L, 1L, null, false);
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(taskDTO)
                .post("/tarefas")
                .then()
                .statusCode(500);
    }

    @Override
    @Test
    @Order(4)
    @Transactional
    public void testGetAll() {
        RestAssured.given()
                .get("/tarefas")
                .then()
                .statusCode(200)
                .body("", Matchers.hasSize(1));
    }

    @Override
    @Test
    @Order(5)
    @Transactional
    public void testFind() {
        ValidatableResponse response = RestAssured.given()
                .get("/tarefas/1")
                .then()
                .statusCode(200);
        Assertions.assertEquals(response.extract().body().jsonPath().get("title"), TASK_NAME);
    }

    @Override
    @Test
    @Order(6)
    @Transactional
    public void testFindNonexistent() {
        RestAssured.given()
                .get("/tarefas/2")
                .then()
                .statusCode(500);
    }

    @Override
    @Test
    @Order(7)
    @Transactional
    public void testUpdate() {
        ValidatableResponse response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(new TaskDTO(1L, UPDATED_TASK_NAME, null, null, null, null, null, null, null))
                .put("/tarefas")
                .then()
                .statusCode(200);
        Assertions.assertEquals(response.extract().body().jsonPath().get("title"), UPDATED_TASK_NAME);
    }

    @Override
    @Test
    @Order(8)
    @Transactional
    public void testUpdateFail() {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(new TaskDTO(null, UPDATED_TASK_NAME, null, null, null, null, null, null, null))
                .put("/tarefas")
                .then()
                .statusCode(500);
    }

    @Override
    @Test
    @Order(9)
    @Transactional
    public void testUpdateNonexistent() {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(new TaskDTO(2L, UPDATED_TASK_NAME, null, null, null, null, null, null, null))
                .put("/tarefas")
                .then()
                .statusCode(500);
    }

    @Test
    @Order(10)
    @Transactional
    public void testAllocateUser() {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(new UserDTO(null, "Test User", 1L, Collections.emptyList(), 0L))
                .post("/pessoas")
                .then()
                .statusCode(200);
        ValidatableResponse response = RestAssured.given()
                .put("/tarefas/alocar/1")
                .then()
                .statusCode(200);
        Assertions.assertEquals(response.extract().body().jsonPath().getLong("userId"), 1L);
    }

    @Test
    @Order(11)
    @Transactional
    public void testAllocatedNonexistentUser() {
        RestAssured.given()
                .put("/tarefas/alocar/2")
                .then()
                .statusCode(500);
    }

    @Test
    @Order(12)
    @Transactional
    public void testClose() {
        RestAssured.given()
                .put("/tarefas/finalizar/1")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(13)
    @Transactional
    public void testCloseClosed() {
        RestAssured.given()
                .put("/tarefas/finalizar/1")
                .then()
                .statusCode(500);
    }

    @Test
    @Order(14)
    @Transactional
    public void testCloseNonexistent() {
        RestAssured.given()
                .put("/tarefas/finalizar/2")
                .then()
                .statusCode(500);
    }

    @Override
    @Test
    @Order(15)
    @Transactional
    public void testDelete() {
        RestAssured.given()
                .delete("/tarefas/1")
                .then()
                .statusCode(204);
        RestAssured.given()
                .delete("/pessoas/1")
                .then()
                .statusCode(204);
        RestAssured.given()
                .delete("/departamentos/1")
                .then()
                .statusCode(204);
    }

    @Override
    @Test
    @Order(16)
    @Transactional
    public void testDeleteNonexistent() {
        RestAssured.given()
                .delete("/tarefas/2")
                .then()
                .statusCode(500);
    }
}