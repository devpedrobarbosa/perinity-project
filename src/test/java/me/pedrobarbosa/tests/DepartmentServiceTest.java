package me.pedrobarbosa.tests;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import jakarta.transaction.Transactional;
import me.pedrobarbosa.dto.DepartmentDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepartmentServiceTest implements ServiceTest {

    private static final String DEPARTMENT_NAME = "Test ", UPDATED_DEPARTMENT_NAME = "Updated  Name";

    /*
     * Testa se o departamento consegue ser criado fornecendo dados válidos
     * */
    @Override
    @Test
    @Order(1)
    @Transactional
    public void testCreate() {
        ValidatableResponse response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(new DepartmentDTO(null, DEPARTMENT_NAME, null, null))
                .post("/departamentos")
                .then()
                .statusCode(200);
        Assertions.assertEquals(response.extract().body().jsonPath().get("name"), DEPARTMENT_NAME);
    }

    @Override
    @Test
    @Order(2)
    @Transactional
    public void testCreateFail() {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(new DepartmentDTO(null, null, null, null))
                .post("/departamentos")
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
                .body(new DepartmentDTO(null, DEPARTMENT_NAME, null, null))
                .post("/departamentos")
                .then()
                .statusCode(500);
    }

    @Override
    @Test
    @Order(4)
    @Transactional
    public void testGetAll() {
        RestAssured.given()
                .get("/departamentos")
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
                .get("/departamentos/1")
                .then()
                .statusCode(200);
        Assertions.assertEquals(response.extract().body().jsonPath().get("name"), DEPARTMENT_NAME);
    }

    @Override
    @Test
    @Order(6)
    @Transactional
    public void testFindNonexistent() {
        RestAssured.given()
                .get("/departamentos/2")
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
                .body(new DepartmentDTO(1L, UPDATED_DEPARTMENT_NAME, null, null))
                .put("/departamentos")
                .then()
                .statusCode(200);
        Assertions.assertEquals(response.extract().body().jsonPath().get("name"), UPDATED_DEPARTMENT_NAME);
    }

    @Override
    @Test
    @Order(8)
    @Transactional
    public void testUpdateFail() {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(new DepartmentDTO(null, UPDATED_DEPARTMENT_NAME, null, null))
                .put("/departamentos")
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
                .body(new DepartmentDTO(2L, UPDATED_DEPARTMENT_NAME, null, null))
                .put("/departamentos")
                .then()
                .statusCode(500);
    }

    @Override
    @Test
    @Order(10)
    @Transactional
    public void testDelete() {
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
                .delete("/departamentos/2")
                .then()
                .statusCode(500);
    }
}