package me.pedrobarbosa.tests;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import jakarta.transaction.Transactional;
import me.pedrobarbosa.dto.DepartmentDTO;
import me.pedrobarbosa.dto.UserDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.util.Collections;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest implements ServiceTest {

    private static final String USER_NAME = "Usuário Teste", UPDATED_USER_NAME = "Usuário Teste Atualizado";

    @Override
    @Test
    @Order(1)
    @Transactional
    public void testCreate() {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(new DepartmentDTO(null, "Test Department", 0, 0))
                .post("/departamentos")
                .then()
                .statusCode(200);
        ValidatableResponse response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(new UserDTO(null, USER_NAME, 1L, Collections.emptyList(), 0L))
                .post("/pessoas")
                .then()
                .statusCode(200);
        JsonPath path = response.extract().body().jsonPath();
        Assertions.assertEquals(path.get("name"), USER_NAME);
        Assertions.assertEquals(path.getLong("departmentId"), 1L);
    }

    @Override
    @Test
    @Order(2)
    @Transactional
    public void testCreateFail() {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(new UserDTO(null, null, null, null, null))
                .post("/pessoas")
                .then()
                .statusCode(500);
    }

    @Override
    @Test
    @Order(3)
    @Transactional
    public void testCreateExistent() {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(new UserDTO(1L, USER_NAME, 1L, Collections.emptyList(), 0L))
                .post("/pessoas")
                .then()
                .statusCode(500);
    }

    @Override
    @Test
    @Order(4)
    @Transactional
    public void testGetAll() {
        RestAssured.given()
                .get("/pessoas")
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
                .get("/pessoas/1")
                .then()
                .statusCode(200);
        Assertions.assertEquals(response.extract().body().jsonPath().get("name"), USER_NAME);
    }

    @Override
    @Test
    @Order(6)
    @Transactional
    public void testFindNonexistent() {
        RestAssured.given()
                .get("/pessoas/2")
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
                .body(new UserDTO(1L, UPDATED_USER_NAME, null, null, null))
                .put("/pessoas")
                .then()
                .statusCode(200);
        Assertions.assertEquals(response.extract().body().jsonPath().get("name"), UPDATED_USER_NAME);
    }

    @Override
    @Test
    @Order(8)
    @Transactional
    public void testUpdateFail() {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(new UserDTO(null, UPDATED_USER_NAME, null, null, null))
                .put("/pessoas")
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
                .body(new UserDTO(2L, UPDATED_USER_NAME, null, null, null))
                .put("/pessoas")
                .then()
                .statusCode(500);
    }

    @Override
    @Test
    @Order(10)
    @Transactional
    public void testDelete() {
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
    @Order(11)
    @Transactional
    public void testDeleteNonexistent() {
        RestAssured.given()
                .delete("/pessoas/2")
                .then()
                .statusCode(500);
    }
}