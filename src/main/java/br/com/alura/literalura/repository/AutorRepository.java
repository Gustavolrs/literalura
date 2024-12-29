package br.com.alura.literalura.repository;

import br.com.alura.literalura.entity.AutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutorRepository extends JpaRepository<AutorEntity, Long> {
    Optional<AutorEntity> findByNome(String nome);
}
