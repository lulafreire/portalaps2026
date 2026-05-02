package com.lulafreire.apsirece.repository;

import com.lulafreire.apsirece.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

    // Busca o histórico de mensagens entre dois servidores (Chat Individual)
    @Query("SELECT m FROM Mensagem m WHERE " +
            "(m.remetente.email = :email1 AND m.destinatario.email = :email2) OR " +
            "(m.remetente.email = :email2 AND m.destinatario.email = :email1) " +
            "ORDER BY m.dataEnvio ASC")
    List<Mensagem> findChatHistorico(@Param("email1") String email1, @Param("email2") String email2);

    // Busca as mensagens de um grupo específico
    List<Mensagem> findByGrupoIdOrderByDataEnvioAsc(Long grupoId);

    @Query("SELECT m FROM Mensagem m WHERE " +
            "(m.remetente.id = :id1 AND m.destinatario.id = :id2) OR " +
            "(m.remetente.id = :id2 AND m.destinatario.id = :id1) " +
            "ORDER BY m.dataEnvio ASC")
    List<Mensagem> findChatPrivado(@Param("id1") Long id1, @Param("id2") Long id2);

    // CONTAGENS PARA OS BADGES (NÚMEROS VERMELHOS)

    // Total de mensagens não lidas para o menu "Mensagens" na Sidebar
    Long countByDestinatarioEmailAndLidaFalse(String email);

    // Mensagens não lidas de um remetente específico (Para o Aside de Servidores)
    Long countByRemetenteEmailAndDestinatarioEmailAndLidaFalse(String remetenteEmail, String destinatarioEmail);

    // Mensagens não lidas em um grupo específico
    // Nota: Exige lógica adicional se você quiser saber quais membros já leram
    Long countByGrupoIdAndLidaFalse(Long grupoId);
}