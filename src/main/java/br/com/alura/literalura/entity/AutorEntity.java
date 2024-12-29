package br.com.alura.literalura.entity;

import jakarta.persistence.*;

import java.util.List;


@Entity
public class AutorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Certifique-se de que este nome corresponde ao banco de dados.
    private Long id;

    private String nome;



    @Column(name = "ano_nascimento")
    private Integer anoDeNascimento;

    @Column(name = "ano_falecimento")
    private Integer anoDeFalecimento;

    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<LivroEntity> livros;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getAnoDeNascimento() {
        return anoDeNascimento;
    }

    public void setAnoDeNascimento(Integer anoDeNascimento) {
        this.anoDeNascimento = anoDeNascimento;
    }

    public Integer getAnoDeFalecimento() {
        return anoDeFalecimento;
    }

    public void setAnoDeFalecimento(Integer anoDeFalecimento) {
        this.anoDeFalecimento = anoDeFalecimento;
    }

    public List<LivroEntity> getLivros() {
        return livros;
    }

    public void setLivros(List<LivroEntity> livros) {
        this.livros = livros;
    }

}

