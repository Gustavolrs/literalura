package br.com.alura.literalura;

import br.com.alura.literalura.entity.AutorEntity;
import br.com.alura.literalura.entity.LivroEntity;
import br.com.alura.literalura.service.GutendexService;
import br.com.alura.literalura.service.GutendexService.Livro;
import br.com.alura.literalura.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    @Autowired
    private GutendexService gutendexService;

    @Autowired
    private LivroService livroService;

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) {
        exibirMenu();
    }

    // ===================== MENU PRINCIPAL =====================
    private void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            imprimirMenu();
            opcao = lerInteiroValido(scanner, "Digite sua escolha: ");
            scanner.nextLine(); // Consumir quebra de linha pendente

            switch (opcao) {
                case 1 -> buscarLivros(scanner);
                case 2 -> listarTodosLivros();
                case 3 -> listarTodosAutores();
                case 4 -> listarAutoresVivos(scanner);
                case 5 -> filtrarPorIdioma(scanner);
                case 6 -> System.out.println("Saindo do sistema. Até logo!");
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 6);
    }

    private void imprimirMenu() {
        System.out.println("\n========== Bem-vindo ao Literalura ==========");
        System.out.println("1 - Buscar livros pelo título");
        System.out.println("2 - Listar livros registrados");
        System.out.println("3 - Listar autores registrados");
        System.out.println("4 - Listar autores vivos em um determinado ano");
        System.out.println("5 - Listar livros em um determinado idioma");
        System.out.println("6 - Sair");
        System.out.println("============================================");
    }

    private int lerInteiroValido(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, insira um número válido.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    // ===================== FUNCIONALIDADES =====================
    private void buscarLivros(Scanner scanner) {
        System.out.print("Digite o título exato do livro: ");
        String termo = scanner.nextLine();

        List<Livro> livros = gutendexService.buscarLivros(termo).stream()
                .filter(livro -> termo.equalsIgnoreCase(livro.getTitle()))
                .toList();

        if (!livros.isEmpty()) {
            System.out.println("\nLivros encontrados:");
            for (Livro livro : livros) {
                // Verificar se o livro já existe no banco
                boolean existeNoBanco = livroService.listarTodosLivros().stream()
                        .anyMatch(l -> l.getTitulo().equalsIgnoreCase(livro.getTitle()));

                if (!existeNoBanco) {
                    livroService.salvarLivro(livro);
                }
                System.out.println(formataLivro(livro));
            }
        } else {
            System.out.println("Nenhum livro encontrado com o título exato informado.");
        }
    }
    private void listarTodosLivros() {
        List<LivroEntity> livros = livroService.listarTodosLivros();
        if (livros.isEmpty()) {
            System.out.println("\nNenhum livro registrado no banco.");
        } else {
            System.out.println("\n========== LIVROS REGISTRADOS ==========");
            System.out.println("==========================================");
            livros.forEach(livro -> {
                System.out.println("----------------- LIVRO ------------------");
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Idioma: " + livro.getIdioma());
                System.out.println("Downloads: " + livro.getNumeroDeDownloads());
                System.out.println("Autor: " + livro.getAutor().getNome());
                System.out.println("------------------------------------------");
            });
            System.out.println("==========================================");
        }
    }




    private void listarTodosAutores() {
        List<AutorEntity> autores = livroService.listarTodosAutores();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado no banco.");
        } else {
            System.out.println("\n========== AUTORES REGISTRADOS ==========");
            autores.forEach(autor -> {
                System.out.printf("Nome: %s\n", autor.getNome());
                System.out.printf("  Ano de Nascimento: %s\n",
                        autor.getAnoDeNascimento() != null ? autor.getAnoDeNascimento() : "Desconhecido");
                System.out.printf("  Ano de Falecimento: %s\n",
                        autor.getAnoDeFalecimento() != null ? autor.getAnoDeFalecimento() : "Ainda vivo");

                List<LivroEntity> livrosDoAutor = autor.getLivros();
                if (livrosDoAutor != null && !livrosDoAutor.isEmpty()) {
                    System.out.println("  Livros:");
                    livrosDoAutor.forEach(livro -> System.out.printf("    - %s\n", livro.getTitulo()));
                } else {
                    System.out.println("  Nenhum livro registrado para este autor.");
                }
                System.out.println("----------------------------------------");
            });
        }
    }


    private void listarAutoresVivos(Scanner scanner) {
        int ano = lerInteiroValido(scanner, "Digite o ano para verificar autores vivos: ");

        List<AutorEntity> autoresVivos = livroService.listarTodosAutores().stream()
                .filter(autor -> {
                    Integer nascimento = autor.getAnoDeNascimento();
                    Integer falecimento = autor.getAnoDeFalecimento();
                    return nascimento != null && nascimento <= ano &&
                            (falecimento == null || falecimento > ano);
                })
                .toList();

        if (autoresVivos.isEmpty()) {
            System.out.println("Nenhum autor vivo encontrado no ano informado.");
        } else {
            System.out.println("\n========== AUTORES VIVOS NO ANO " + ano + " ==========");
            autoresVivos.forEach(autor -> {
                System.out.printf("Nome: %s\n", autor.getNome());
                System.out.printf("  Ano de Nascimento: %s\n",
                        autor.getAnoDeNascimento() != null ? autor.getAnoDeNascimento() : "Desconhecido");
                System.out.printf("  Ano de Falecimento: %s\n",
                        autor.getAnoDeFalecimento() != null ? autor.getAnoDeFalecimento() : "Ainda vivo");

                List<LivroEntity> livrosDoAutor = autor.getLivros();
                if (livrosDoAutor != null && !livrosDoAutor.isEmpty()) {
                    System.out.println("  Livros:");
                    livrosDoAutor.forEach(livro -> System.out.printf("    - %s\n", livro.getTitulo()));
                } else {
                    System.out.println("  Nenhum livro registrado para este autor.");
                }
                System.out.println("----------------------------------------");
            });
        }
    }

    private void filtrarPorIdioma(Scanner scanner) {
        System.out.println("\n========== FILTRAR POR IDIOMA ==========");
        System.out.println("1 - es (Espanhol)");
        System.out.println("2 - en (Inglês)");
        System.out.println("3 - fr (Francês)");
        System.out.println("4 - pt (Português)");
        int escolha = lerInteiroValido(scanner, "Escolha uma opção: ");
        scanner.nextLine(); // Consumir quebra de linha pendente

        String idioma = switch (escolha) {
            case 1 -> "es";
            case 2 -> "en";
            case 3 -> "fr";
            case 4 -> "pt";
            default -> {
                System.out.println("Opção inválida. Retornando ao menu principal.");
                yield null;
            }
        };

        if (idioma != null) {
            List<LivroEntity> livros = livroService.listarPorIdioma(idioma);
            if (livros.isEmpty()) {
                System.out.println("Nenhum livro encontrado para o idioma informado.");
            } else {
                System.out.println("\n========== LIVROS NO IDIOMA '" + idioma + "' ==========");
                livros.forEach(livro -> {
                    System.out.printf("Título: %s\n", livro.getTitulo());
                    System.out.printf("  Idioma: %s\n", livro.getIdioma());
                    System.out.printf("  Downloads: %d\n", livro.getNumeroDeDownloads());
                    System.out.printf("  Autor: %s\n", livro.getAutor().getNome()); // Exibindo o nome do autor
                    System.out.println("----------------------------------------");
                });
            }
        }
    }


    // ===================== UTILITÁRIOS =====================
    private String formataLivro(Livro livro) {
        return String.format("Título: %s, Autor: %s, Idioma: %s, Número de downloads: %d",
                livro.getTitle(),
                livro.getAutorPrincipal() != null ? livro.getAutorPrincipal().getName() : "Desconhecido",
                livro.getIdiomas() != null && !livro.getIdiomas().isEmpty() ? livro.getIdiomas().get(0) : "Desconhecido",
                livro.getNumeroDeDownloads());
    }
}
