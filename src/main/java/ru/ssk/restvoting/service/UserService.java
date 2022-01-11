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
import ru.ssk.restvoting.repository.UserRepository;
import ru.ssk.restvoting.util.exception.UserDeleteViolationException;
import ru.ssk.restvoting.util.exception.UserUpdateViolationException;
import ru.ssk.restvoting.web.user.AuthUser;

import java.util.List;

import static ru.ssk.restvoting.util.UserUtil.prepareToSave;
import static ru.ssk.restvoting.util.ValidationUtil.checkNew;
import static ru.ssk.restvoting.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {

    private final String CANT_DELETE_USER_MSG_CODE = "exception.user.cantDeleteUser";
    private final String CANT_UPDATE_USER_MSG_CODE = "exception.user.cantUpdateUser";
    private final int MAX_PREDEFINED_USER_ID = 1002;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReloadableResourceBundleMessageSource messageSource;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ReloadableResourceBundleMessageSource messageSource) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
    }

    public User get(int id) {
        return checkNotFoundWithId(userRepository.get(id), id);
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public void update(User user) {
        Assert.notNull(user, "User must not be null");
        Assert.notNull(user.getId(), "User id must not be null");
        int userId = user.getId();
        checkUserUpdateRestrictions(userId);
        User original = userRepository.get(userId);
        checkNotFoundWithId(original, userId);
        user.setRoles(original.getRoles());
        userRepository.save(prepareToSave(user, passwordEncoder));
    }

    public User create(User user) {
        Assert.notNull(user, "User must not be null");
        checkNew(user);
        return userRepository.save(prepareToSave(user, passwordEncoder));
    }

    public void delete(int id) {
        checkUserDeleteRestrictions(id);
        checkNotFoundWithId(userRepository.delete(id), id);
    }

    private void checkUserDeleteRestrictions(int userId) {
        if (userId <= MAX_PREDEFINED_USER_ID) {
          throw new UserDeleteViolationException(messageSource.getMessage(CANT_DELETE_USER_MSG_CODE, null, LocaleContextHolder.getLocale()));
        }
    }

    private void checkUserUpdateRestrictions(int userId) {
        if (userId <= MAX_PREDEFINED_USER_ID) {
            throw new UserUpdateViolationException(messageSource.getMessage(CANT_UPDATE_USER_MSG_CODE, null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByNameCaseInsensitive(username);
        if (user == null) {
            throw new UsernameNotFoundException("User '"+ username + "' is not found");
        }
        return new AuthUser(user);
    }
}
