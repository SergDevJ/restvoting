package ru.ssk.restvoting.web;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.ssk.restvoting.TestUtil.userAuth;
import static ru.ssk.restvoting.UserTestData.*;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void rootAdmin() throws Exception {
        mockMvc.perform(get("/")
                .with(userAuth(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index_admin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/index_admin.jsp"));
    }

    @Test
    void rootUser() throws Exception {
        mockMvc.perform(get("/")
                .with(userAuth(user)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/voting"));
    }

    @Test
    void wrongUser() throws Exception {
        mockMvc.perform(get("/")
                .with(userAuth(invalidUser)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void voting() throws Exception {
        mockMvc.perform(get("/voting")
                .with(userAuth(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("voting"))
                .andExpect(model().attributeExists("restaurants"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/voting.jsp"));
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(get("/login?message=test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().size(2))
                .andExpect(model().attribute("message","test"))
                .andExpect(model().attributeExists("error", "message"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/login.jsp"));
    }

    @Test
    void logoutPostNoCsrf() throws Exception {
        mockMvc.perform(post("/logout")
                .with(userAuth(user)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void register() throws Exception {
        mockMvc.perform(get("/profile/register"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("register",true))
                .andExpect(forwardedUrl("/WEB-INF/jsp/profile.jsp"));
    }

    @Test
    void profile() throws Exception {
        mockMvc.perform(get("/profile")
                .with(userAuth(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/profile.jsp"));
    }
}