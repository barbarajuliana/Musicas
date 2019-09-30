package com.example.demo;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MusicaController {

	private MusicaRepository repositorio;

	public MusicaController(MusicaRepository repositorio) {
		this.repositorio = repositorio;
	}
	
	@GetMapping("/")
	public String paginaInicial() {

		return "index";
	}
	
	@GetMapping("/novo")
	public String exibirForm(Model model) {
		Musica musica = new Musica();
		model.addAttribute("musica", musica);

		String[] genero = {"Forró", "Samba", "Funk", "Rock", "Gospel"};

		model.addAttribute("todosGenero", genero);

		return "formulario";
	}
	
	@PostMapping("/cadastrar")
	public String cadastrar(@ModelAttribute @Valid Musica musica, BindingResult bindingResult) {
		musica.setNome(musica.getNome().toUpperCase());
		musica.setArtista(musica.getArtista().toUpperCase());
		musica.setGenero(musica.getGenero().toUpperCase());
		musica.setAlbum(musica.getAlbum().toUpperCase());
		musica.setLancamento(musica.getLancamento());
		if (bindingResult.hasErrors()) {
			return "formulario";
		} else {
			repositorio.save(musica);
			return "redirect:/listar";
		}
	}

	
	@GetMapping("/listar")
	public String listar(Model model) {
		Iterable<Musica> musicas = repositorio.findAll();

		model.addAttribute("musicas", musicas);

		return "listarMusica";
	}

	
	@GetMapping("/exibir")
	public String exibir(Integer id, Model model) {
		Optional<Musica> musica = repositorio.findById(id);

		model.addAttribute("musica", musica.get());

		return "dadosMusicas";
	}

	@GetMapping("/excluir")
	public String excluir(Integer id) {
		repositorio.deleteById(id);

		return "redirect:/listar";
	}
	

	@GetMapping("/editar")
	public String editar(Integer id, Model model) {
		Optional<Musica> musica = repositorio.findById(id);
		
		String[] genero = {"Forró", "Samba", "Funk", "Rock", "Gospel"};

		model.addAttribute("todosGenero", genero);

		model.addAttribute("musica", musica);

		return "editarMusica";

	}

	
}
	


