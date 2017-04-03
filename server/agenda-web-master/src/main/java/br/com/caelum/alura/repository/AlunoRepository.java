package br.com.caelum.alura.repository;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.caelum.alura.model.Aluno;

public interface AlunoRepository extends PagingAndSortingRepository<Aluno, String> {

	@Query("FROM Aluno a WHERE a.modificacao > :datahora")
	List<Aluno> modificados(@Param("datahora") LocalDateTime datahora);

	@Query("FROM Aluno a WHERE a.desativado = 0")
	List<Aluno> visiveis();

	@Query("SELECT COUNT(a) > 0 FROM Aluno a WHERE a.modificacao > :datahora")
	boolean existeAtualizacao(@Param("datahora") LocalDateTime datahora);

	@Query("SELECT COUNT(a) > 0 FROM Aluno a WHERE a.idCliente = :idCliente")
	boolean existeIdCliente(@Param("idCliente") int idCliente);
}
