package one.digitalinnovation.gof.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import one.digitalinnovation.gof.dto.ClienteRequest;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 * 
 * @author falvojr
 */
@Service
public class ClienteServiceImpl implements ClienteService {

	// Singleton: Injetar os componentes do Spring com @Autowired.
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;
	
	// Strategy: Implementar os métodos definidos na interface.
	// Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

	@Override
	public Iterable<Cliente> buscarTodos() {
		// Buscar somente Clientes que nao foram excluidos logicamente.
		return clienteRepository.findByAtivoTrue();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		// Buscar Cliente por ID.
		return clienteRepository.findById(id)
				.filter(Cliente::isAtivo)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encontrado"));
	}

	@Override
	public void inserir(ClienteRequest clienteRequest) {
		Cliente cliente = converterParaCliente(clienteRequest);
		salvarClienteComCep(cliente);
	}

	@Override
	public void atualizar(Long id, ClienteRequest clienteRequest) {
		// Buscar Cliente por ID, caso exista:
		Optional<Cliente> clienteBd = clienteRepository.findById(id)
				.filter(Cliente::isAtivo);
		if (clienteBd.isPresent()) {
			Cliente cliente = clienteBd.get();
			cliente.setNome(clienteRequest.getNome());
			cliente.setEndereco(converterParaCliente(clienteRequest).getEndereco());
			salvarClienteComCep(cliente);
		}
	}

	@Override
	public void deletar(Long id) {
		// Exclusao logica: preserva o registro e apenas o marca como inativo.
		clienteRepository.findById(id).ifPresent(cliente -> {
			cliente.setAtivo(false);
			clienteRepository.save(cliente);
		});
	}

	private Cliente converterParaCliente(ClienteRequest clienteRequest) {
		Endereco endereco = new Endereco();
		endereco.setCep(clienteRequest.getCep());

		Cliente cliente = new Cliente();
		cliente.setNome(clienteRequest.getNome());
		cliente.setEndereco(endereco);
		return cliente;
	}

	private void salvarClienteComCep(Cliente cliente) {
		// Verificar se o Endereco do Cliente já existe (pelo CEP).
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			// Caso não exista, integrar com o ViaCEP e persistir o retorno.
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		// Inserir Cliente, vinculando o Endereco (novo ou existente).
		clienteRepository.save(cliente);
	}

}
