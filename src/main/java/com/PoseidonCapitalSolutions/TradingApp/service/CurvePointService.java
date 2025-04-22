package com.PoseidonCapitalSolutions.TradingApp.service;

import com.PoseidonCapitalSolutions.TradingApp.domain.CurvePoint;
import com.PoseidonCapitalSolutions.TradingApp.dto.CurvePointDTO;
import com.PoseidonCapitalSolutions.TradingApp.exception.ResourceNotFoundException;
import com.PoseidonCapitalSolutions.TradingApp.mapper.CurvePointMapper;
import com.PoseidonCapitalSolutions.TradingApp.repository.CurvePointRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type Curve point service.
 */
@AllArgsConstructor
@Service
public class CurvePointService {

    private final CurvePointRepository curvePointRepository;
    private final CurvePointMapper curvePointMapper;

    /**
     * Find by id curve point dto.
     *
     * @param id the id
     * @return the curve point dto
     */
    public CurvePointDTO findById(Integer id) {
        return curvePointRepository.findById(id)
                .map(curvePointMapper::toCurvePointDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid curve point id: " + id));
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<CurvePointDTO> findAll() {
        return curvePointRepository.findAll().stream()
                .map(curvePointMapper::toCurvePointDTO)
                .toList();
    }

    /**
     * Create.
     *
     * @param curvePointDTO the curve point dto
     */
    @Transactional
    public void create(CurvePointDTO curvePointDTO) {
        curvePointRepository.save(curvePointMapper.toCurvePoint(curvePointDTO));
    }

    /**
     * Update.
     *
     * @param id                   the id
     * @param updatedCurvePointDTO the updated curve point dto
     */
    @Transactional
    public void update(Integer id, CurvePointDTO updatedCurvePointDTO) {
        CurvePoint existingCurvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid curve point id: " + id));

        curvePointMapper.updateCurvePointFromDto(updatedCurvePointDTO, existingCurvePoint);
        curvePointRepository.save(existingCurvePoint);
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(Integer id) {
        CurvePoint curvePointToDelete = curvePointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid curve point id: " + id));

        curvePointRepository.delete(curvePointToDelete);
    }
}
