package com.analisedecredito.aplicacao_analise_credito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.analisedecredito.aplicacao_analise_credito.dto.AnaliseRestricaoDto;
import com.analisedecredito.aplicacao_analise_credito.model.AnaliseRestricao;
import com.analisedecredito.aplicacao_analise_credito.model.Cliente;
import com.analisedecredito.aplicacao_analise_credito.repository.AnaliseRestricaoRepository;
import com.analisedecredito.aplicacao_analise_credito.repository.ClienteRepository;

@Service
public class AnaliseRestricaoService {

    @Autowired
    AnaliseRestricaoRepository repository;

    @Autowired
    ClienteRepository clienteRepository;

    /* Retorna uma analise de restrição de acordo com o id */
    public AnaliseRestricaoDto findById(Integer id) {
        return new AnaliseRestricaoDto(repository.findById(id).get());
    }

    /* Retorna uma lista de análises de restrições cadastrados */
    public List<AnaliseRestricaoDto> list() {
        List<AnaliseRestricao> listaAnalises = repository.findAll();
        return listaAnalises.stream().map(AnaliseRestricaoDto::new).toList();
    }

    /* Cria uma nova análise de restrição com base nos dados fornecidos */
    public void create(AnaliseRestricaoDto analiseRestricaoDto) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(analiseRestricaoDto.getCliente());
        if (clienteOpt.isPresent()) {
            AnaliseRestricao analise = new AnaliseRestricao(analiseRestricaoDto, clienteOpt.get());
            repository.save(analise);
        }
    }

    /* Atualiza os dados de uma análise de restrição existente */
    public AnaliseRestricaoDto update(Integer id, AnaliseRestricaoDto analiseRestricaoDto) {
        Optional<AnaliseRestricao> analiseOpt = repository.findById(id);
        if (analiseOpt.isPresent()) {
            AnaliseRestricao analiseRestricao = analiseOpt.get();
            Optional<Cliente> clienteOpt = clienteRepository.findById(analiseRestricaoDto.getCliente());
            if (clienteOpt.isPresent()) {
                Cliente cliente = clienteOpt.get();
                analiseRestricao.setCliente(cliente);
                analiseRestricao.setStatusSerasa(analiseRestricaoDto.getStatusSerasa());
                analiseRestricao.setStatusSpc(analiseRestricaoDto.getStatusSpc());
                AnaliseRestricao updatedAnaliseRestricao = repository.save(analiseRestricao);
                return new AnaliseRestricaoDto(updatedAnaliseRestricao);
            } else {
                throw new ResourceNotFoundException(
                        "Perfil de crédito não encontrado com id " + analiseRestricaoDto.getCliente());
            }
        } else {
            throw new ResourceNotFoundException("Cliente não encontrado com id " + id);
        }
    }

    /* Remove uma análise de restrição pelo id */
    public void delete(Integer id) {
        AnaliseRestricao analiseRestricao = repository.findById(id).get();
        repository.delete(analiseRestricao);
    }

}
