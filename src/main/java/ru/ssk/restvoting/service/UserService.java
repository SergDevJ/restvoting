package ru.ssk.restvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.ssk.restvoting.model.User;
import ru.ssk.restvoting.repository.UserDataJpaRepository;
import ru.ssk.restvoting.util.exception.UserDeleteViolationException;
import ru.ssk.restvoting.util.exception.UserUpdateViolationException;
import ru.ssk.restvoting.web.user.AuthUser;

import java.util.List;

import static ru.ssk.restvoting.util.UserUtil.prepareToSave;
import static ru.ssk.restvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {
    private final String CANT_DELETE_USER_MSG_CODE = "exception.user.cantDeleteUser";
    private final String CANT_UPDATE_USER_MSG_CODE = "exception.user.cantUpdateUser";
    private final int MAX_PREDEFINED_USER_ID = 1002;

    private final UserDataJpaRepository crudRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReloadableResourceBundleMessageSource messageSource;

    @Autowired
    public UserService(UserDataJpaRepository userRepository, PasswordEncoder passwordEncoder, ReloadableResourceBundleMessageSource messageSource) {
        this.crudRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
    }

    public User get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    public List<User> getAll() {
        return crudRepository.findAll();
    }

    public User getReference(int id) {
        return crudRepository.getById(id);
    }

    public void update(User user) {
        Assert.notNull(user, "User must not be null");
        Assert.notNull(user.getId(), "User id must not be null");
        int userId = user.getId();
        checkUpdateRestrictions(userId);
        User original = get(userId);
        checkNotFoundWithId(original, userId);
        user.setRoles(original.getRoles());
        crudRepository.save(prepareToSave(user, passwordEncoder));
    }

    public User create(User user) {
        Assert.notNull(user, "User must not be null");
        return crudRepository.save(prepareToSave(user, passwordEncoder));
    }

    public void delete(int id) {
        checkDeleteRestrictions(id);
        checkNotFoundWithId(crudRepository.delete(id) != 0, id);
    }

    private void checkDeleteRestrictions(int userId) {
        if (userId <= MAX_PREDEFINED_USER_ID) {
          throw new UserDeleteViolationException(messageSource.getMessage(CANT_DELETE_USER_MSG_CODE, null, LocaleContextHolder.getLocale()));
        }
    }

    private void checkUpdateRestrictions(int userId) {
        if (userId <= MAX_PREDEFINED_USER_ID) {
            throw new UserUpdateViolationException(messageSource.getMessage(CANT_UPDATE_USER_MSG_CODE, null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = crudRepository.getByNameCaseInsensitive(username);
        if (user == null) {
            throw new UsernameNotFoundException("User '"+ username + "' is not found");
        }
        return new AuthUser(user);
    }
}
