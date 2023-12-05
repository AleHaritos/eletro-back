package eletro.repository;

import eletro.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query(value = "SELECT * FROM pedidos  where data BETWEEN :dtInicio AND :dtFim order by pedidos.id", nativeQuery = true)
    List<Pedido> findPedidosByData(@Param("dtInicio") Date dtInicio, @Param("dtFim") Date dtFim);


    @Query(value = "SELECT data FROM pedidos where date_part('year', pedidos.data) = :dtYear", nativeQuery = true)
    List<Date> getDateYear(@Param("dtYear") Integer dtYear);
}
