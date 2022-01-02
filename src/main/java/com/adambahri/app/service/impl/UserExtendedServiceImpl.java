package com.adambahri.app.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.adambahri.app.domain.UserExtended;
import com.adambahri.app.repository.UserExtendedRepository;
import com.adambahri.app.repository.search.UserExtendedSearchRepository;
import com.adambahri.app.service.UserExtendedService;
import com.adambahri.app.service.dto.UserExtendedDTO;
import com.adambahri.app.service.mapper.UserExtendedMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserExtended}.
 */
@Service
@Transactional
public class UserExtendedServiceImpl implements UserExtendedService {

    private final Logger log = LoggerFactory.getLogger(UserExtendedServiceImpl.class);

    private final UserExtendedRepository userExtendedRepository;

    private final UserExtendedMapper userExtendedMapper;

    private final UserExtendedSearchRepository userExtendedSearchRepository;

    public UserExtendedServiceImpl(
        UserExtendedRepository userExtendedRepository,
        UserExtendedMapper userExtendedMapper,
        UserExtendedSearchRepository userExtendedSearchRepository
    ) {
        this.userExtendedRepository = userExtendedRepository;
        this.userExtendedMapper = userExtendedMapper;
        this.userExtendedSearchRepository = userExtendedSearchRepository;
    }

    @Override
    public UserExtendedDTO save(UserExtendedDTO userExtendedDTO) {
        log.debug("Request to save UserExtended : {}", userExtendedDTO);
        UserExtended userExtended = userExtendedMapper.toEntity(userExtendedDTO);
        userExtended = userExtendedRepository.save(userExtended);
        UserExtendedDTO result = userExtendedMapper.toDto(userExtended);
        userExtendedSearchRepository.save(userExtended);
        return result;
    }

    @Override
    public Optional<UserExtendedDTO> partialUpdate(UserExtendedDTO userExtendedDTO) {
        log.debug("Request to partially update UserExtended : {}", userExtendedDTO);

        return userExtendedRepository
            .findById(userExtendedDTO.getId())
            .map(existingUserExtended -> {
                userExtendedMapper.partialUpdate(existingUserExtended, userExtendedDTO);

                return existingUserExtended;
            })
            .map(userExtendedRepository::save)
            .map(savedUserExtended -> {
                userExtendedSearchRepository.save(savedUserExtended);

                return savedUserExtended;
            })
            .map(userExtendedMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserExtendedDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserExtendeds");
        return userExtendedRepository.findAll(pageable).map(userExtendedMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserExtendedDTO> findOne(Long id) {
        log.debug("Request to get UserExtended : {}", id);
        return userExtendedRepository.findById(id).map(userExtendedMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserExtended : {}", id);
        userExtendedRepository.deleteById(id);
        userExtendedSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserExtendedDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserExtendeds for query {}", query);
        return userExtendedSearchRepository.search(query, pageable).map(userExtendedMapper::toDto);
    }
}
