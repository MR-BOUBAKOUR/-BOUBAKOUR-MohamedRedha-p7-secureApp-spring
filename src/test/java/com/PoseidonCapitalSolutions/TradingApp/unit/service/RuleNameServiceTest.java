package com.PoseidonCapitalSolutions.TradingApp.unit.service;

import com.PoseidonCapitalSolutions.TradingApp.domain.RuleName;
import com.PoseidonCapitalSolutions.TradingApp.dto.RuleNameDTO;
import com.PoseidonCapitalSolutions.TradingApp.exception.ResourceNotFoundException;
import com.PoseidonCapitalSolutions.TradingApp.mapper.RuleNameMapper;
import com.PoseidonCapitalSolutions.TradingApp.repository.RuleNameRepository;
import com.PoseidonCapitalSolutions.TradingApp.service.RuleNameService;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RuleNameServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @Mock
    private RuleNameMapper ruleNameMapper;

    @InjectMocks
    private RuleNameService ruleNameService;

    private AutoCloseable closeable;
    private Integer id;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        id = 1;
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void findById_success() {
        RuleName entity = new RuleName();
        RuleNameDTO dto = new RuleNameDTO();

        when(ruleNameRepository.findById(id)).thenReturn(Optional.of(entity));
        when(ruleNameMapper.toRuleNameDTO(entity)).thenReturn(dto);

        RuleNameDTO result = ruleNameService.findById(id);

        assertEquals(dto, result);
        verify(ruleNameRepository).findById(id);
        verify(ruleNameMapper).toRuleNameDTO(entity);
    }

    @Test
    void findById_exception() {
        when(ruleNameRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ruleNameService.findById(id));
        verify(ruleNameRepository).findById(id);
    }

    @Test
    void findAll_success() {
        List<RuleName> entities = List.of(new RuleName(), new RuleName());

        when(ruleNameRepository.findAll()).thenReturn(entities);
        when(ruleNameMapper.toRuleNameDTO(any())).thenReturn(new RuleNameDTO());

        List<RuleNameDTO> result = ruleNameService.findAll();

        assertEquals(2, result.size());
        verify(ruleNameRepository).findAll();
        verify(ruleNameMapper, times(2)).toRuleNameDTO(any());
    }

    @Test
    void create_success() {
        RuleNameDTO dto = new RuleNameDTO();
        RuleName entity = new RuleName();

        when(ruleNameMapper.toRuleName(dto)).thenReturn(entity);

        ruleNameService.create(dto);

        verify(ruleNameMapper).toRuleName(dto);
        verify(ruleNameRepository).save(entity);
    }

    @Test
    void update_success() {
        RuleNameDTO dto = new RuleNameDTO();
        RuleName existingEntity = new RuleName();

        when(ruleNameRepository.findById(id)).thenReturn(Optional.of(existingEntity));

        ruleNameService.update(id, dto);

        verify(ruleNameMapper).updateRuleNameFromDto(dto, existingEntity);
        verify(ruleNameRepository).save(existingEntity);
    }

    @Test
    void update_exception() {
        RuleNameDTO dto = new RuleNameDTO();

        when(ruleNameRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ruleNameService.update(id, dto));
        verify(ruleNameRepository).findById(id);
    }

    @Test
    void delete_success() {
        ruleNameService.delete(id);

        verify(ruleNameRepository).deleteById(id);
    }
}