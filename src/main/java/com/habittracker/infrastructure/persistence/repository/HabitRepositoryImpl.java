package com.habittracker.infrastructure.persistence.repository;

import com.habittracker.domain.entity.Habit;
import com.habittracker.domain.repository.HabitRepository;
import com.habittracker.domain.valueobject.HabitId;
import com.habittracker.domain.valueobject.UserId;
import com.habittracker.infrastructure.persistence.entity.HabitJpaEntity;
import com.habittracker.infrastructure.persistence.mapper.HabitJpaDomainMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class HabitRepositoryImpl implements HabitRepository {

    private final HabitJpaRepository jpaRepository;
    private final HabitJpaDomainMapper mapper;

    @Override
    public Habit save(Habit habit) {
        log.debug("Salvando hábito: {}", habit.getId());

        try {
            var existingEntity = jpaRepository.findById(habit.getId().value());
            HabitJpaEntity jpaEntity;

            if (existingEntity.isPresent()) {
                jpaEntity = existingEntity.get();
                mapper.updateJpaEntity(jpaEntity, habit);
                log.debug("Atualizando hábito existente: {}", habit.getId());
            } else {
                jpaEntity = mapper.toJpaEntity(habit);
                log.debug("Salvando hábito existente: {}", habit.getId());
            }

            var savedHabit = jpaRepository.save(jpaEntity);
            var savedDomain = mapper.toDomain(savedHabit);

            log.debug("Hábito salvo com sucesso: {}", habit.getId());

            return savedDomain;
        } catch (Exception e) {
            log.error("Erro ao salvar hábito {}: {}", habit.getId(), e.getMessage(), e);
            throw new RuntimeException("Erro ao salvar hábito", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Habit> findById(HabitId habitId) {
        log.debug("Encontrando hábito por ID: {}", habitId);

        try {
            return jpaRepository.findById(habitId.value())
                    .map(mapper::toDomain);
        } catch (Exception e) {
            log.error("Erro ao buscar hábito {}: {}", habitId, e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar hábito", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Habit> findActiveByUserId(UserId userId) {
        log.debug("Encontrando hábitos ativos para o usuário: {}", userId);

        try {
            return jpaRepository.findByUserIdAndActiveTrue(userId.value())
                    .stream()
                    .map(mapper::toDomain)
                    .toList();
        } catch(Exception e) {
            log.error("Erro ao buscar hábitos ativos para o usuário {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar hábitos ativos", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Habit> findAllByUserId(UserId userId) {
        log.debug("Buscando todos os hábitos do usuário: {}", userId);

        try {
            return jpaRepository.findByUserId(userId.value())
                    .stream()
                    .map(mapper::toDomain)
                    .toList();
        } catch(Exception e) {
            log.error("Erro ao buscar hábitos para o usuário {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar hábitos", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserIdAndName(UserId userId, String name) {
        log.debug("Verificando existência do hábito '{}' para usuário: {}", name, userId);

        try {
            var exists = jpaRepository.existsByUserIdAndName(userId.value(), name);
            log.debug("Hábito '{}' existe para o usuário {}: {}", name, userId, exists);
            return exists;
        } catch(Exception e) {
            log.error("Erro ao verificar a existência de hábito '{}' para o usuário {}: {}",
                    name, userId, e.getMessage(), e);
            throw new RuntimeException("Erro ao verificar a existência de hábito", e);
        }
    }

    @Override
    public void deleteById(HabitId habitId) {
        log.debug("Deletando um habito {}", habitId);

        try {
            jpaRepository.deleteById(habitId.value());
            log.debug("Hábito {} deletado com sucesso.", habitId);
        } catch(Exception e) {
            log.error("Erro ao deletar hábito {}: {}", habitId, e.getMessage(), e);
            throw new RuntimeException("Erro aaao deletaar hábito.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveByUserId(UserId userId) {
        log.debug("Recuperando quantidaade de hábitos ativos para o usuário: {}", userId);

        try {
            long activeHabitsCount = jpaRepository.countByUserIdAndActiveTrue(userId.value());
            log.debug("Hábitos ativos encontrados {} para o usuário: {}", activeHabitsCount, userId);
            return activeHabitsCount;
        } catch(Exception e) {
            log.error("Erro ao encontrar quantidade de hábitos ativos para o usuário {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Erro ao encontrar quantidade de hábitos ativos.", e);
        }
    }
}
