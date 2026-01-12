package sg.edu.nus.iss.d13revision;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import sg.edu.nus.iss.d13revision.controllers.PersonController;
import sg.edu.nus.iss.d13revision.models.Person;
import sg.edu.nus.iss.d13revision.services.PersonService;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    private List<Person> personList;

    @BeforeEach
    void setUp() {
        personList = new ArrayList<>();
        personList.add(new Person("John", "Doe"));
        personList.add(new Person("Jane", "Doe"));
    }

    @Test
    void testGetPersons() throws Exception {
        when(personService.getPersons()).thenReturn(personList);

        mockMvc.perform(get("/person/personList"))
                .andExpect(status().isOk())
                .andExpect(view().name("personList"))
                .andExpect(model().attribute("persons", personList));
    }

    @Test
    void testShowAddPersonForm() throws Exception {
        mockMvc.perform(get("/person/addPerson"))
                .andExpect(status().isOk())
                .andExpect(view().name("addPerson"))
                .andExpect(model().attributeExists("personForm"));
    }

    @Test
    void testAddPerson() throws Exception {
        mockMvc.perform(post("/person/addPerson")
                .param("firstName", "Test")
                .param("lastName", "User"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/person/personList"));
    }

    @Test
    void testAddPerson_Invalid() throws Exception {
        mockMvc.perform(post("/person/addPerson")
                .param("firstName", "")
                .param("lastName", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("addPerson"))
                .andExpect(model().attributeExists("errorMessage"));
    }
    
    @Test
    void testUpdatePerson() throws Exception {
        mockMvc.perform(post("/person/personEdit")
                .flashAttr("per", new Person("John", "Doe")))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/person/personList"));
    }

    @Test
    void testDeletePerson() throws Exception {
        mockMvc.perform(post("/person/personDelete")
                .flashAttr("per", new Person("John", "Doe")))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/person/personList"));
    }
}
