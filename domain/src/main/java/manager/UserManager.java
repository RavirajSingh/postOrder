package manager;


import entity.UserEntity;
import lombok.AllArgsConstructor;
import model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@AllArgsConstructor
public class UserManager {
    private UserRepository repository;

    @Transactional
    public User create(User user) throws Exception {
        UserEntity entity = convertToEntity(user, null);
        entity = repository.save(entity);
        return convertToEntry(entity);
    }

    @Transactional
    public User update(Long id, User user) throws Exception {
        UserEntity entity = repository.findOne(id);
        entity = convertToEntity(user, entity);
        return convertToEntry(entity);
    }

    @Cacheable(value = "userCache")
    @Transactional(readOnly = true)
    public User findById(Long id) throws Exception {
        UserEntity entity = repository.findOne(id);
        if (Objects.isNull(entity)) {
            throw new Exception("No user found for Id " + id);
        }
        return convertToEntry(entity);
    }

    @Cacheable(value = "userCache")
    @Transactional(readOnly = true)
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        repository.findAll().forEach(x -> users.add(convertToEntry(x)));
        return users;
    }

    @Transactional
    public void delete(Long id) throws Exception {
        repository.delete(id);
    }

    private User convertToEntry(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        return user;
    }

    private UserEntity convertToEntity(User user, UserEntity entity) {
        if (Objects.isNull(entity)) {
            entity = new UserEntity();
        }
        entity.setName(user.getName());
        entity.setId(user.getId());
        return entity;
    }
}
