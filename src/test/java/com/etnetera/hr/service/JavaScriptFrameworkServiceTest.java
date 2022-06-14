package com.etnetera.hr.service;

import com.etnetera.hr.data.HypeLevel;
import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.dto.JavaScriptFrameworkDTO;
import com.etnetera.hr.exception.JavascriptFrameworkDuplicateException;
import com.etnetera.hr.exception.JavascriptFrameworkNotFoundException;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaScriptFrameworkServiceTest {

    @Autowired
    private JavaScriptFrameworkService service;

    @MockBean
    private JavaScriptFrameworkRepository repository;

    @Test(expected = JavascriptFrameworkNotFoundException.class)
    public void shouldThrowWhenDeleteByIdNotFound() {

        when(repository.existsById(anyLong())).thenReturn(false);

        service.delete(999L);
    }

    @Test(expected = JavascriptFrameworkNotFoundException.class)
    public void shouldThrowWhenUpdateByIdNotFound() {

        when(repository.existsById(anyLong())).thenReturn(true);

        service.update(999L, createDto());
    }

    @Test(expected = JavascriptFrameworkDuplicateException.class)
    public void shouldThrowWhenCreatingDuplicateFramework() {

        when(repository.findByName(anyString())).thenReturn(Optional.of(createFramework(1L)));

        service.createFramework(createDto());
    }

    @Test(expected = JavascriptFrameworkDuplicateException.class)
    public void shouldThrowWhenCreatingDuplicateVersion() {

        when(repository.findByName(anyString())).thenReturn(Optional.of(createFramework(1L)));

        service.addVersion("TestFramework", "1.0");
    }

    @Test
    public void shouldNotFailWhenFindReturnNone() {

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        var framework = service.findFrameworkById(999L);

        assertEquals(Optional.empty(), framework);
    }

    @Test
    public void testAddVersion() {

        when(repository.findByName(anyString())).thenReturn(Optional.of(createFramework(1L)));

        service.addVersion("TestFramework", "999.0");

        verify(repository, times(1)).save(any(JavaScriptFramework.class));
    }

    @Test
    public void testFindAll() {

        when(repository.findAll()).thenReturn(List.of(createFramework(1), createFramework(2)));

        var dtoList = service.findAllFrameworks();

        assertEquals(2, dtoList.size());

        var dto = dtoList.get(0);

        assertEquals("TestFramework 1", dto.getName());
        assertEquals(Set.of("1.0", "1.0.1", "2", "1.2"), dto.getVersion());
        assertNull(dto.getDeprecationDate());
        assertEquals(Long.valueOf(1L), dto.getId());
        assertEquals(HypeLevel.HIGH, dto.getHypeLevel());
    }

    @Test
    public void testSearch() {

        when(repository.findAll(Mockito.< Specification<JavaScriptFramework> >any())).thenReturn(List.of(createFramework(1), createFramework(2)));

        var dtoList = service.search("Test", null, null, null);

        assertEquals(2, dtoList.size());
    }

    private JavaScriptFrameworkDTO createDto() {
        return new JavaScriptFrameworkDTO(1L, "TestFramework", Set.of("1.0"), LocalDate.now(), HypeLevel.NONE);
    }

    private JavaScriptFramework createFramework(long id) {
        var framework = new JavaScriptFramework();
        framework.setId(id);
        framework.setName("TestFramework " + id);
        framework.setVersion(Set.of("1.0", "1.0.1", "1.2", "2"));
        framework.setDeprecationDate(null);
        framework.setHypeLevel(HypeLevel.HIGH);

        return framework;
    }
}
