package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.dto.request.CargaRequest;
import com.agendamento.bff.v1.domain.model.Carga;
import com.agendamento.bff.v1.domain.model.CargaProduto;
import com.agendamento.bff.v1.domain.model.EspecieCarga;
import com.agendamento.bff.v1.domain.model.Pedido;
import com.agendamento.bff.v1.domain.model.Produto;
import com.agendamento.bff.v1.domain.model.StatusCarga;
import com.agendamento.bff.v1.domain.model.TipoCarga;
import com.agendamento.bff.v1.domain.model.TipoVeiculo;
import com.agendamento.bff.v1.domain.model.Transportador;
import com.agendamento.bff.v1.domain.model.Usuario;
import com.agendamento.bff.v1.domain.dto.request.CargaProdutoRequest;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.CargaProdutoRepository;
import com.agendamento.bff.v1.repository.CargaRepository;
import com.agendamento.bff.v1.repository.EspecieCargaRepository;
import com.agendamento.bff.v1.repository.PedidoRepository;
import com.agendamento.bff.v1.repository.ProdutoRepository;
import com.agendamento.bff.v1.repository.StatusCargaRepository;
import com.agendamento.bff.v1.repository.TipoCargaRepository;
import com.agendamento.bff.v1.repository.TipoVeiculoRepository;
import com.agendamento.bff.v1.repository.TransportadorRepository;
import com.agendamento.bff.v1.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CargaService {

    private final CargaRepository cargaRepository;
    private final CargaProdutoRepository cargaProdutoRepository;
    private final StatusCargaRepository statusCargaRepository;
    private final TipoCargaRepository tipoCargaRepository;
    private final TipoVeiculoRepository tipoVeiculoRepository;
    private final TransportadorRepository transportadorRepository;
    private final EspecieCargaRepository especieCargaRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Carga> listar() {
        return cargaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Carga buscarPorId(Long id) {
        return cargaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carga nao encontrada. id=" + id));
    }

    @Transactional
    public Carga criar(Carga nova, CargaRequest req) {
        if (nova.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }

        validarCamposObrigatorios(req);
        aplicarRelacionamentos(nova, req);
        return cargaRepository.save(nova);
    }

    @Transactional
    public Carga atualizar(Long id, Carga dados, CargaRequest req) {
        Carga atual = buscarPorId(id);

        if (dados.getDataAgendamento() != null) {
            atual.setDataAgendamento(dados.getDataAgendamento());
        }

        if (req.idStatusCarga() != null) {
            atual.setStatusCarga(buscarStatusCarga(req.idStatusCarga()));
        }
        if (req.idTipoCarga() != null) {
            atual.setTipoCarga(buscarTipoCarga(req.idTipoCarga()));
        }
        if (req.idTipoVeiculo() != null) {
            atual.setTipoVeiculo(buscarTipoVeiculo(req.idTipoVeiculo()));
        }
        if (req.idTransportadora() != null) {
            atual.setTransportador(buscarTransportador(req.idTransportadora()));
        }
        if (req.idEspecieCarga() != null) {
            atual.setEspecieCarga(buscarEspecieCarga(req.idEspecieCarga()));
        }
        if (req.idUsuarioSolicitante() != null) {
            atual.setUsuarioSolicitante(buscarUsuario(req.idUsuarioSolicitante()));
        }
        if (req.idUsuarioAprovador() != null) {
            atual.setUsuarioAprovador(buscarUsuario(req.idUsuarioAprovador()));
        }

        validarCargaComRelacionamentos(atual);
        return cargaRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id) {
        Carga carga = buscarPorId(id);
        cargaRepository.delete(carga);
    }

    @Transactional(readOnly = true)
    public List<CargaProduto> listarProdutosDaCarga(Long cargaId) {
        buscarPorId(cargaId);
        return cargaProdutoRepository.findAllByCargaId(cargaId);
    }

    @Transactional
    public CargaProduto adicionarProdutoNaCarga(Long cargaId, CargaProduto novo, CargaProdutoRequest req) {
        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }
        validarCargaProdutoRequest(req);

        if (cargaProdutoRepository.existsByCargaIdAndProdutoIdAndPedidoId(cargaId, req.idProduto(), req.idPedido())) {
            throw new IllegalArgumentException("Este produto/pedido ja foi vinculado a carga informada.");
        }

        Carga carga = buscarPorId(cargaId);
        Produto produto = buscarProduto(req.idProduto());
        Pedido pedido = buscarPedido(req.idPedido());

        novo.setCarga(carga);
        novo.setProduto(produto);
        novo.setPedido(pedido);
        novo.setQuantidadeAlocada(req.quantidadeAlocada());
        novo.setData(req.data() != null ? req.data() : LocalDateTime.now());

        return cargaProdutoRepository.save(novo);
    }

    @Transactional
    public CargaProduto editarProdutoNaCarga(Long cargaId, Long cargaProdutoId, CargaProduto dados, CargaProdutoRequest req) {
        CargaProduto atual = cargaProdutoRepository.findByIdAndCargaId(cargaProdutoId, cargaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto da carga nao encontrado. cargaId=" + cargaId + ", id=" + cargaProdutoId
                ));

        if (req.idProduto() != null) {
            atual.setProduto(buscarProduto(req.idProduto()));
        }
        if (req.idPedido() != null) {
            atual.setPedido(buscarPedido(req.idPedido()));
        }
        if (dados.getQuantidadeAlocada() != null) {
            validarQuantidadeAlocada(dados.getQuantidadeAlocada());
            atual.setQuantidadeAlocada(dados.getQuantidadeAlocada());
        }
        if (dados.getData() != null) {
            atual.setData(dados.getData());
        }

        return cargaProdutoRepository.save(atual);
    }

    @Transactional
    public void removerProdutoDaCarga(Long cargaId, Long cargaProdutoId) {
        CargaProduto atual = cargaProdutoRepository.findByIdAndCargaId(cargaProdutoId, cargaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto da carga nao encontrado. cargaId=" + cargaId + ", id=" + cargaProdutoId
                ));
        cargaProdutoRepository.delete(atual);
    }

    private void validarCamposObrigatorios(CargaRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("O corpo da requisicao e obrigatorio.");
        }
        if (req.idStatusCarga() == null) {
            throw new IllegalArgumentException("O campo 'idStatusCarga' e obrigatorio.");
        }
        if (req.idTipoCarga() == null) {
            throw new IllegalArgumentException("O campo 'idTipoCarga' e obrigatorio.");
        }
        if (req.idTipoVeiculo() == null) {
            throw new IllegalArgumentException("O campo 'idTipoVeiculo' e obrigatorio.");
        }
        if (req.idTransportadora() == null) {
            throw new IllegalArgumentException("O campo 'idTransportadora' e obrigatorio.");
        }
        if (req.idEspecieCarga() == null) {
            throw new IllegalArgumentException("O campo 'idEspecieCarga' e obrigatorio.");
        }
        if (req.idUsuarioSolicitante() == null) {
            throw new IllegalArgumentException("O campo 'idUsuarioSolicitante' e obrigatorio.");
        }
        if (req.idUsuarioAprovador() != null) {
            throw new IllegalArgumentException("O campo 'idUsuarioAprovador' deve ser informado apenas no PUT.");
        }
        if (req.dataAgendamento() == null) {
            throw new IllegalArgumentException("O campo 'dataAgendamento' e obrigatorio.");
        }
    }

    private void aplicarRelacionamentos(Carga carga, CargaRequest req) {
        carga.setStatusCarga(buscarStatusCarga(req.idStatusCarga()));
        carga.setTipoCarga(buscarTipoCarga(req.idTipoCarga()));
        carga.setTipoVeiculo(buscarTipoVeiculo(req.idTipoVeiculo()));
        carga.setTransportador(buscarTransportador(req.idTransportadora()));
        carga.setEspecieCarga(buscarEspecieCarga(req.idEspecieCarga()));
        carga.setUsuarioSolicitante(buscarUsuario(req.idUsuarioSolicitante()));
        carga.setUsuarioAprovador(null);
        carga.setDataAgendamento(req.dataAgendamento());

        validarCargaComRelacionamentos(carga);
    }

    private void validarCargaComRelacionamentos(Carga carga) {
        if (carga.getStatusCarga() == null) {
            throw new IllegalArgumentException("O campo 'idStatusCarga' e obrigatorio.");
        }
        if (carga.getTipoCarga() == null) {
            throw new IllegalArgumentException("O campo 'idTipoCarga' e obrigatorio.");
        }
        if (carga.getTipoVeiculo() == null) {
            throw new IllegalArgumentException("O campo 'idTipoVeiculo' e obrigatorio.");
        }
        if (carga.getTransportador() == null) {
            throw new IllegalArgumentException("O campo 'idTransportadora' e obrigatorio.");
        }
        if (carga.getEspecieCarga() == null) {
            throw new IllegalArgumentException("O campo 'idEspecieCarga' e obrigatorio.");
        }
        if (carga.getUsuarioSolicitante() == null) {
            throw new IllegalArgumentException("O campo 'idUsuarioSolicitante' e obrigatorio.");
        }
        if (carga.getDataAgendamento() == null) {
            throw new IllegalArgumentException("O campo 'dataAgendamento' e obrigatorio.");
        }
    }

    private StatusCarga buscarStatusCarga(Long id) {
        return statusCargaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StatusCarga nao encontrado. id=" + id));
    }

    private TipoCarga buscarTipoCarga(Long id) {
        return tipoCargaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoCarga nao encontrado. id=" + id));
    }

    private TipoVeiculo buscarTipoVeiculo(Long id) {
        return tipoVeiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoVeiculo nao encontrado. id=" + id));
    }

    private Transportador buscarTransportador(Long id) {
        return transportadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transportador nao encontrado. id=" + id));
    }

    private EspecieCarga buscarEspecieCarga(Long id) {
        return especieCargaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EspecieCarga nao encontrada. id=" + id));
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado. id=" + id));
    }

    private Produto buscarProduto(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado. id=" + id));
    }

    private Pedido buscarPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido nao encontrado. id=" + id));
    }

    private void validarCargaProdutoRequest(CargaProdutoRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("O corpo da requisicao e obrigatorio.");
        }
        if (req.idProduto() == null) {
            throw new IllegalArgumentException("O campo 'idProduto' e obrigatorio.");
        }
        if (req.idPedido() == null) {
            throw new IllegalArgumentException("O campo 'idPedido' e obrigatorio.");
        }
        if (req.quantidadeAlocada() == null) {
            throw new IllegalArgumentException("O campo 'quantidadeAlocada' e obrigatorio.");
        }
        validarQuantidadeAlocada(req.quantidadeAlocada());
    }

    private void validarQuantidadeAlocada(BigDecimal quantidadeAlocada) {
        if (quantidadeAlocada.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O campo 'quantidadeAlocada' deve ser maior que zero.");
        }
    }
}
