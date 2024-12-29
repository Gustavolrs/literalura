package br.com.alura.literalura.entity;

import jakarta.persistence.*;

@Entity
public class LivroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Certifique-se de que este nome corresponde ao banco de dados.
    private Long id;


    private String titulo;

    private String idioma;

    @Column(name = "numero_downloads")
    private int numeroDeDownloads;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private AutorEntity autor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public int getNumeroDeDownloads() {
        return numeroDeDownloads;
    }

    public void setNumeroDeDownloads(int numeroDeDownloads) {
        this.numeroDeDownloads = numeroDeDownloads;
    }

    public AutorEntity getAutor() {
        return autor;
    }

    public void setAutor(AutorEntity autor) {
        this.autor = autor;
    }


}
