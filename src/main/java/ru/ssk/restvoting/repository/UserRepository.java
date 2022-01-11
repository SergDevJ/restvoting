package ru.ssk.restvoting.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ssk.restvoting.model.User;

import java.util.List;

@Repository
public class UserRepository {
    private final UserDataJpaRepository crudRepository;

    @Autowired
    public UserRepository(UserDataJpaRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Transactional
    public User save(User user) {
        return crudRepository.save(user);
    }

    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    public User get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    public List<User> getAll() {
        return crudRepository.findAll();
    }

    public User getUserByNameCaseInsensitive(String name) {
        return crudRepository.getUserByNameCaseInsensitive(name);
    }

    public User getByEmail(String email) {
        return crudRepository.getByEmail(email);
    }

    public User getByName(String name) {
        return crudRepository.getByName(name);
    }

    public User getReference(int id) {
        return crudRepository.getById(id);
    }

}
