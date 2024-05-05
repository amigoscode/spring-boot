package com.amigoscode.person;


import com.amigoscode.SortingOrder;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
class PersonServiceTest {

    @MockBean
    private FakePersonRepository fakePersonRepository;

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Test
    void canGetAllPeople() {
        // given
        SortingOrder sort = SortingOrder.ASC;
        // when
        personService.getPeople(sort);
        // then
        ArgumentCaptor<Sort> sortCaptor =
                ArgumentCaptor.forClass(Sort.class);

        verify(personRepository).findAll(sortCaptor.capture());
        assertThat(sortCaptor.getValue())
                .isEqualTo(Sort.by(
                        Sort.Direction.valueOf(sort.name()),
                        "id"
                ));
    }
}