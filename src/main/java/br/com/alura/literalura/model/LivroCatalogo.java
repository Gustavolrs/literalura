// Modificações nas classes LivroCatalogo, GutendexService e nos modelos serão implementadas para refletir as mudanças descritas acima.

package br.com.alura.literalura.model;

import br.com.alura.literalura.service.GutendexService.Livro;
import br.com.alura.literalura.service.GutendexService.Autor;
import br.com.alura.literalura.service.LivroService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LivroCatalogo {
    private static final List<Livro> catalogo = new ArrayList<>();

    // Adicionar um livro ao catálogo
    public static void adicionarLivro(Livro livro) {
        catalogo.add(livro);
    }

    // Listar todos os livros
    public static List<Livro> listarTodos() {
        return new ArrayList<>(catalogo);
    }

    // Modificar listarPorIdioma para verificar todos os idiomas
    public static List<Livro> listarPorIdioma(String idioma) {
        return catalogo.stream()
                .filter(livro -> livro.getIdiomas() != null && livro.getIdiomas().contains(idioma))
                .collect(Collectors.toList());
    }


    // Listar todos os autores com informações detalhadas
    public static void listarTodosOsAutoresDetalhado() {
        System.out.println("Autores dos livros buscados:");
        for (Livro livro : catalogo) {
            Autor autor = livro.getAutorPrincipal();
            if (autor != null) {
                System.out.println("Autor: " + autor.getName());
                System.out.println("Ano de nascimento: " + (autor.getAnoDeNascimento() != null ? autor.getAnoDeNascimento() : "N/A"));
                System.out.println("Ano de falecimento: " + (autor.getAnoDeFalecimento() != null ? autor.getAnoDeFalecimento() : "N/A"));
                System.out.println("Livros: " + livro.getTitle());
                System.out.println();
            }
        }
    }

    public static void carregarDoBanco(LivroService livroService) {
        catalogo.addAll(livroService.listarTodosLivros().stream()
                .map(livroEntity -> {
                    Livro livro = new Livro();
                    livro.setTitle(livroEntity.getTitulo());
                    livro.setIdiomas(List.of(livroEntity.getIdioma()));
                    livro.setNumeroDeDownloads(livroEntity.getNumeroDeDownloads());
                    // Mapear Autor
                    return livro;
                })
                .collect(Collectors.toList()));
    }


    // Listar autores vivos em um determinado ano com informações detalhadas
    public static void listarAutoresVivosEmAnoDetalhado(int ano) {
        System.out.println("Autores vivos no ano " + ano + ":");
        for (Livro livro : catalogo) {
            Autor autor = livro.getAutorPrincipal();
            if (autor != null) {
                Integer nascimento = autor.getAnoDeNascimento();
                Integer falecimento = autor.getAnoDeFalecimento();

                if (nascimento != null && nascimento <= ano &&
                        (falecimento == null || falecimento > ano)) {
                    System.out.println("Autor: " + autor.getName());
                    System.out.println("Ano de nascimento: " + nascimento);
                    System.out.println("Ano de falecimento: " + (falecimento != null ? falecimento : "Ainda vivo"));
                    System.out.println("Livros: " + livro.getTitle());
                    System.out.println();
                }
            }
        }
    }
}