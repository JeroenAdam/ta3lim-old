package com.adambahri.app.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.adambahri.app.domain.Votes;
import com.adambahri.app.repository.VotesRepository;
import com.adambahri.app.repository.search.VotesSearchRepository;
import com.adambahri.app.service.VotesService;
import com.adambahri.app.service.dto.VotesDTO;
import com.adambahri.app.service.mapper.VotesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Votes}.
 */
@Service
@Transactional
public class VotesServiceImpl implements VotesService {

    private final Logger log = LoggerFactory.getLogger(VotesServiceImpl.class);

    private final VotesRepository votesRepository;

    private final VotesMapper votesMapper;

    private final VotesSearchRepository votesSearchRepository;

    public VotesServiceImpl(VotesRepository votesRepository, VotesMapper votesMapper, VotesSearchRepository votesSearchRepository) {
        this.votesRepository = votesRepository;
        this.votesMapper = votesMapper;
        this.votesSearchRepository = votesSearchRepository;
    }

    @Override
    public VotesDTO save(VotesDTO votesDTO) {
        log.debug("Request to save Votes : {}", votesDTO);
        Votes votes = votesMapper.toEntity(votesDTO);
        votes = votesRepository.save(votes);
        VotesDTO result = votesMapper.toDto(votes);
        votesSearchRepository.save(votes);
        return result;
    }

    @Override
    public Optional<VotesDTO> partialUpdate(VotesDTO votesDTO) {
        log.debug("Request to partially update Votes : {}", votesDTO);

        return votesRepository
            .findById(votesDTO.getId())
            .map(existingVotes -> {
                votesMapper.partialUpdate(existingVotes, votesDTO);

                return existingVotes;
            })
            .map(votesRepository::save)
            .map(savedVotes -> {
                votesSearchRepository.save(savedVotes);

                return savedVotes;
            })
            .map(votesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VotesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Votes");
        return votesRepository.findAll(pageable).map(votesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VotesDTO> findOne(Long id) {
        log.debug("Request to get Votes : {}", id);
        return votesRepository.findById(id).map(votesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Votes : {}", id);
        votesRepository.deleteById(id);
        votesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VotesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Votes for query {}", query);
        return votesSearchRepository.search(query, pageable).map(votesMapper::toDto);
    }
}
