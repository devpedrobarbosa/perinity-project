package me.pedrobarbosa.tests;

public interface ServiceTest {

    /*
     * Testa se é possível criar uma entidade sem problemas
     * */
    void testCreate();

    /*
     * Testa-se o caso de erro onde, por má formação do objeto, não é possível inserir a entidade
     * */
    void testCreateFail();

    /*
     * Testa o caso de erro onde tenta-se criar uma entidade que já existe
     * */
    void testCreateExistent();

    /*
     * Testa se é possível retornar a lista de todas as entidades existentes sem problemas
     * */
    void testGetAll();

    /*
     * Testa se é possível encontrar uma entidade existente sem problemas
     * */
    void testFind();

    /*
     * Testa o caso de erro onde tenta-se encontrar uma entidade não existente
     * */
    void testFindNonexistent();

    /*
     * Testa se é possível atualizar uma entidade existente sem problemas
     * */
    void testUpdate();

    /*
     * Testa o caso de erro onde, por má formação do objeto, não é possível atualizar uma entidade
     * */
    void testUpdateFail();

    /*
     * Testa o caso de erro onde tenta-se atualizar uma entidade não existente
     * */
    void testUpdateNonexistent();

    /*
     * Testa se é possível deletar uma entidade existente sem problemas
     * */
    void testDelete();

    /*
     * Testa o caso de erro onde tenta-se deletar uma entidade não existente
     * */
    void testDeleteNonexistent();
}