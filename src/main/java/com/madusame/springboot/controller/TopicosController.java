package com.madusame.springboot.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.madusame.springboot.controller.DTO.DetailTopicoDto;
import com.madusame.springboot.controller.DTO.TopicoDto;
import com.madusame.springboot.form.AtualizarTopicoForm;
import com.madusame.springboot.form.TopicoForm;
import com.madusame.springboot.modelo.Topico;
import com.madusame.springboot.repository.CursoRepository;
import com.madusame.springboot.repository.TopicoRepository;
import com.madusame.springboot.repository.UsuarioRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepo;

	@Autowired
	private CursoRepository cursoRepo;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@GetMapping
	@Cacheable(value = "listarTodos")
	public Page<TopicoDto> listar(@RequestParam(required = false) String nomeCurso,
			@PageableDefault(sort = "id", direction= Direction.DESC,
			page = 0, size = 3) Pageable pageable) {
		if (nomeCurso == null) {
			Page<Topico> topico = this.topicoRepo.findAll(pageable);
			return TopicoDto.convert(topico);
		}
		return TopicoDto.convert(this.topicoRepo.findByCurso_Nome(nomeCurso,pageable));
	}

	@PostMapping
	@Transactional
	@CacheEvict(value = "listarTodos",allEntries = true)
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(this.cursoRepo);
		topico.setAutor(this.usuarioRepo.findById(1l).get());
		this.topicoRepo.save(topico);

		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

	@GetMapping("/{id}")
	public ResponseEntity<DetailTopicoDto> detail(@PathVariable Long id) {
		Optional<Topico> opcional = this.topicoRepo.findById(id);
		
		if (opcional.isPresent()) {
			return ResponseEntity.ok(new DetailTopicoDto(opcional.get()));
		}
		
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listarTodos",allEntries = true)
	public ResponseEntity<TopicoDto> editar(@PathVariable Long id, @RequestBody @Valid AtualizarTopicoForm form) {
		Optional<Topico> opcional = this.topicoRepo.findById(id);
		
		if (opcional.isPresent()) {
			Topico topico = form.atualizar(id, this.topicoRepo);
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listarTodos",allEntries = true)
	public ResponseEntity<?> deletar(@PathVariable Long id) {
		Optional<Topico> opcional = this.topicoRepo.findById(id);
		
		if (opcional.isPresent()) {
			this.topicoRepo.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
}
