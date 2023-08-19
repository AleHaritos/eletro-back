package eletro.repository;

import eletro.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    @Query("select p from Produto p left join fetch p.imagens where p.id = :id")
    Optional<Produto> findProdutoWithImage(@Param("id") Integer id);

    List<Produto> findFirst20ByOffGreaterThanAndPrecoGreaterThanAndEstoqueGreaterThanOrderByIdDesc(Integer number, Double number1, Integer i);

    List<Produto> findFirst20ByOffLessThanEqualOrOffIsNullAndPrecoIsNullOrPrecoLessThanEqualAndEstoqueGreaterThanOrderByIdDesc(Integer number, Double number1, Integer i);

    List<Produto> findByCategoriaAndEstoqueIsGreaterThanOrderById(String categoria, Integer i);

    List<Produto> findAllByOrderById();

    @Query(value = "select * from produto where categoria = ?1 and estoque > 0 order by produto.id desc limit ?2 offset ?3 ", nativeQuery = true)
    Optional<List<Produto>> findByCategoriasPageable(String categoria, int size, int page);

    @Query(value = "select count(*) from produto where categoria = ?1 and estoque > 0", nativeQuery = true)
    Integer countProdutosByCategoria(String categoria);

    @Modifying
    @Query(value = "update produto  set estoque = ?1 where id = ?2", nativeQuery = true)
    int updateEstoque(Integer estoque, Integer id);
}
