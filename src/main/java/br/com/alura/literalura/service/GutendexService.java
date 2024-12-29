package br.com.alura.literalura.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class GutendexService {

    private static final String BASE_URL = "https://gutendex.com/books/?search=";

    public List<Livro> buscarLivros(String termoDeBusca) {
        try {
            // Codificar o termo de busca para suportar espaços e caracteres especiais
            String url = BASE_URL + URLEncoder.encode(termoDeBusca, StandardCharsets.UTF_8);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return processarRespostaJson(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<Livro> processarRespostaJson(String json) {
        List<Livro> livros = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode resultsNode = rootNode.path("results");

            for (JsonNode livroNode : resultsNode) {
                Livro livro = objectMapper.treeToValue(livroNode, Livro.class);
                // Filtrar apenas o primeiro idioma
                if (livro.getIdiomas() != null && !livro.getIdiomas().isEmpty()) {
                    livro.setIdiomas(List.of(livro.getIdiomas().get(0)));
                }
                livros.add(livro);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return livros;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Livro {
        private String title;

        @JsonAlias("authors")
        private List<Autor> autores;

        @JsonAlias("languages")
        private List<String> idiomas;

        @JsonAlias("download_count") // Nome do campo no JSON
        private int numeroDeDownloads;

        // Getters e Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Autor> getAutores() {
            return autores;
        }

        public void setAutores(List<Autor> autores) {
            this.autores = autores;
        }

        public List<String> getIdiomas() {
            return idiomas;
        }

        public void setIdiomas(List<String> idiomas) {
            this.idiomas = idiomas;
        }

        public int getNumeroDeDownloads() {
            return numeroDeDownloads;
        }

        public void setNumeroDeDownloads(int numeroDeDownloads) {
            this.numeroDeDownloads = numeroDeDownloads;
        }

        public Autor getAutorPrincipal() {
            return autores != null && !autores.isEmpty() ? autores.get(0) : null;
        }




        @Override
        public String toString() {
            return "Título: " + title + '\n' +
                    "Autor: " + (autores != null && !autores.isEmpty() ? autores.get(0).getName() : "N/A") + '\n' +
                    "Idioma: " + (idiomas != null && !idiomas.isEmpty() ? idiomas.get(0) : "N/A") + '\n' +
                    "Número de downloads: " + numeroDeDownloads;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Autor {

        private String name;

        @JsonAlias("birth_year")
        private Integer anoDeNascimento;

        @JsonAlias("death_year")
        private Integer anoDeFalecimento;

        // Getters e Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        @Override
        public String toString() {
            return "Autor: " + name + '\n' +
                    "Ano de nascimento: " + (anoDeNascimento != null ? anoDeNascimento : "N/A") + '\n' +
                    "Ano de falecimento: " + (anoDeFalecimento != null ? anoDeFalecimento : "Ainda vivo");
        }
    }
}