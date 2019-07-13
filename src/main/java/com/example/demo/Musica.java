package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Musica {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@NotBlank
	@Size(min=2, max = 60)
	private String nome;
	
	@NotBlank
	private String artista;
	
	private String genero;
	
	@NotBlank
	private String album;
	
	
	private int lancamento;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getArtista() {
		return artista;
	}
	public void setArtista(String artista) {
		this.artista = artista;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	
	public int getLancamento() {
		return lancamento;
	}
	public void setLancamento(int lancamento) {
		this.lancamento = lancamento;
	}
	
	public Musica() {
		
	}
	
	
	
}
