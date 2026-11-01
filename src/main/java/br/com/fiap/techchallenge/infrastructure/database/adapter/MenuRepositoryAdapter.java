package br.com.fiap.techchallenge.infrastructure.database.adapter;

import br.com.fiap.techchallenge.domain.model.Menu;
import br.com.fiap.techchallenge.infrastructure.database.entity.MenuEntity;
import br.com.fiap.techchallenge.infrastructure.database.jpa.MenuJpaRepository;
import br.com.fiap.techchallenge.usecase.port.output.MenuRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MenuRepositoryAdapter implements MenuRepositoryPort {

    private final MenuJpaRepository menuJpaRepository;

    public MenuRepositoryAdapter(MenuJpaRepository menuJpaRepository) {
        this.menuJpaRepository = menuJpaRepository;
    }

    @Override
    public Menu save(Menu menu) {
        var entity = MenuEntity.fromDomain(menu);
        return menuJpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return menuJpaRepository.findById(id).map(MenuEntity::toDomain);
    }

    @Override
    public List<Menu> findAll() {
        return menuJpaRepository.findAll().stream().map(MenuEntity::toDomain).toList();
    }

    @Override
    public List<Menu> findAllByRestauranteId(Long restauranteId) {
        return menuJpaRepository.findByRestauranteId(restauranteId).stream().map(MenuEntity::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        menuJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return menuJpaRepository.existsById(id);
    }
}
