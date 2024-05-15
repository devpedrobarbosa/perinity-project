package me.pedrobarbosa.service;

import java.util.List;

public interface Service<DTO> {

    /*
     * Processa a requisição e retorna uma lista com todas as entidades
     * */
    List<DTO> getAll();

    /*
     * Processa a requisição e localiza uma entidade pelo ID
     * */
    DTO find(Long id);

    /*
     * Processa a requisição e insere uma entidade no banco de dados
     * */
    DTO create(DTO dto);

    /*
     * Processa a requisição e atualiza uma entidade existente no banco de dados
     * */
    DTO update(DTO dto);

    /*
     * Processa a requisição e remove uma entidade do banco de dados através do ID
     * */
    void delete(Long id);
}