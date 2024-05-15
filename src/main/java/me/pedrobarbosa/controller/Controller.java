package me.pedrobarbosa.controller;

import java.util.List;

public interface Controller<DTO> {

    /*
     * Realiza a requisição da lista de todas as entidades
     * */
    List<DTO> getAll();

    /*
     * Realiza a requisição da busca de uma entidade por ID
     * */
    DTO find(Long id);

    /*
     * Realiza a requisição da inserção de uma nova entidade no banco de dados
     * */
    DTO create(DTO dto);

    /*
     * Realiza a requisição da atualização de uma entidade já existente
     * Para ataulizar, precisa-se apenas informar o campo ID e os campos que
     * forem ser alterados, não é necessário preencher todos
     * */
    DTO update(DTO dto);

    /*
     * Realiza a requisição da remoção de uma entidade existente do banco de dados através do ID
     * */
    void delete(Long id);
}