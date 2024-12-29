package br.com.alura.literalura.service;
import br.com.alura.literalura.entity.AutorEntity;
import br.com.alura.literalura.entity.LivroEntity;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.GutendexService.Livro;
import br.com.alura.literalura.service.GutendexService.Autor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public List<LivroEntity> listarPorIdioma(String idioma) {
        return livroRepository.findByIdioma(idioma);
    }


    public LivroEntity salvarLivro(Livro livro) {
        Autor autorInfo = livro.getAutorPrincipal();

        AutorEntity autorEntity = autorRepository.findByNome(autorInfo.getName())
                .orElseGet(() -> {
                    AutorEntity novoAutor = new AutorEntity();
                    novoAutor.setNome(autorInfo.getName());
                    novoAutor.setAnoDeNascimento(autorInfo.getAnoDeNascimento());
                    novoAutor.setAnoDeFalecimento(autorInfo.getAnoDeFalecimento());
                    return autorRepository.save(novoAutor);
                });

        LivroEntity livroEntity = new LivroEntity();
        livroEntity.setTitulo(livro.getTitle());
        livroEntity.setIdioma(livro.getIdiomas().get(0));
        livroEntity.setNumeroDeDownloads(livro.getNumeroDeDownloads());
        livroEntity.setAutor(autorEntity);

        return livroRepository.save(livroEntity);
    }



    public List<LivroEntity> listarTodosLivros() {
        return livroRepository.findAll();
    }

    public List<AutorEntity> listarTodosAutores() {
        return autorRepository.findAll();
    }
}
