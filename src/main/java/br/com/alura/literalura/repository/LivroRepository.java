package br.com.alura.literalura.repository;

import br.com.alura.literalura.entity.LivroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<LivroEntity, Long> {
    List<LivroEntity> findByIdioma(String idioma);
}

