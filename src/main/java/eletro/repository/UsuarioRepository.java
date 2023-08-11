package eletro.repository;

import eletro.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByEmailAndSenha(String email, String senha);

    @Query(value = "select admin from usuario where email = :email", nativeQuery = true)
    Boolean findUsuarioByEmail(@Param("email") String email);

}
