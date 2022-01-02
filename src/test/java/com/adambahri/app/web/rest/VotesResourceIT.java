package com.adambahri.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adambahri.app.IntegrationTest;
import com.adambahri.app.domain.Resource;
import com.adambahri.app.domain.User;
import com.adambahri.app.domain.Votes;
import com.adambahri.app.repository.VotesRepository;
import com.adambahri.app.repository.search.VotesSearchRepository;
import com.adambahri.app.service.criteria.VotesCriteria;
import com.adambahri.app.service.dto.VotesDTO;
import com.adambahri.app.service.mapper.VotesMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VotesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VotesResourceIT {

    private static final String ENTITY_API_URL = "/api/votes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/votes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VotesRepository votesRepository;

    @Autowired
    private VotesMapper votesMapper;

    /**
     * This repository is mocked in the com.adambahri.app.repository.search test package.
     *
     * @see com.adambahri.app.repository.search.VotesSearchRepositoryMockConfiguration
     */
    @Autowired
    private VotesSearchRepository mockVotesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVotesMockMvc;

    private Votes votes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Votes createEntity(EntityManager em) {
        Votes votes = new Votes();
        return votes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Votes createUpdatedEntity(EntityManager em) {
        Votes votes = new Votes();
        return votes;
    }

    @BeforeEach
    public void initTest() {
        votes = createEntity(em);
    }

    @Test
    @Transactional
    void createVotes() throws Exception {
        int databaseSizeBeforeCreate = votesRepository.findAll().size();
        // Create the Votes
        VotesDTO votesDTO = votesMapper.toDto(votes);
        restVotesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(votesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Votes in the database
        List<Votes> votesList = votesRepository.findAll();
        assertThat(votesList).hasSize(databaseSizeBeforeCreate + 1);
        Votes testVotes = votesList.get(votesList.size() - 1);

        // Validate the Votes in Elasticsearch
        verify(mockVotesSearchRepository, times(1)).save(testVotes);
    }

    @Test
    @Transactional
    void createVotesWithExistingId() throws Exception {
        // Create the Votes with an existing ID
        votes.setId(1L);
        VotesDTO votesDTO = votesMapper.toDto(votes);

        int databaseSizeBeforeCreate = votesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVotesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(votesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Votes in the database
        List<Votes> votesList = votesRepository.findAll();
        assertThat(votesList).hasSize(databaseSizeBeforeCreate);

        // Validate the Votes in Elasticsearch
        verify(mockVotesSearchRepository, times(0)).save(votes);
    }

    @Test
    @Transactional
    void getAllVotes() throws Exception {
        // Initialize the database
        votesRepository.saveAndFlush(votes);

        // Get all the votesList
        restVotesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(votes.getId().intValue())));
    }

    @Test
    @Transactional
    void getVotes() throws Exception {
        // Initialize the database
        votesRepository.saveAndFlush(votes);

        // Get the votes
        restVotesMockMvc
            .perform(get(ENTITY_API_URL_ID, votes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(votes.getId().intValue()));
    }

    @Test
    @Transactional
    void getVotesByIdFiltering() throws Exception {
        // Initialize the database
        votesRepository.saveAndFlush(votes);

        Long id = votes.getId();

        defaultVotesShouldBeFound("id.equals=" + id);
        defaultVotesShouldNotBeFound("id.notEquals=" + id);

        defaultVotesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVotesShouldNotBeFound("id.greaterThan=" + id);

        defaultVotesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVotesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVotesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        votesRepository.saveAndFlush(votes);
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        votes.setUser(user);
        votesRepository.saveAndFlush(votes);
        String userId = user.getId();

        // Get all the votesList where user equals to userId
        defaultVotesShouldBeFound("userId.equals=" + userId);

        // Get all the votesList where user equals to "invalid-id"
        defaultVotesShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    @Test
    @Transactional
    void getAllVotesByResourceIsEqualToSomething() throws Exception {
        // Initialize the database
        votesRepository.saveAndFlush(votes);
        Resource resource;
        if (TestUtil.findAll(em, Resource.class).isEmpty()) {
            resource = ResourceResourceIT.createEntity(em);
            em.persist(resource);
            em.flush();
        } else {
            resource = TestUtil.findAll(em, Resource.class).get(0);
        }
        em.persist(resource);
        em.flush();
        votes.setResource(resource);
        votesRepository.saveAndFlush(votes);
        Long resourceId = resource.getId();

        // Get all the votesList where resource equals to resourceId
        defaultVotesShouldBeFound("resourceId.equals=" + resourceId);

        // Get all the votesList where resource equals to (resourceId + 1)
        defaultVotesShouldNotBeFound("resourceId.equals=" + (resourceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVotesShouldBeFound(String filter) throws Exception {
        restVotesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(votes.getId().intValue())));

        // Check, that the count call also returns 1
        restVotesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVotesShouldNotBeFound(String filter) throws Exception {
        restVotesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVotesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVotes() throws Exception {
        // Get the votes
        restVotesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVotes() throws Exception {
        // Initialize the database
        votesRepository.saveAndFlush(votes);

        int databaseSizeBeforeUpdate = votesRepository.findAll().size();

        // Update the votes
        Votes updatedVotes = votesRepository.findById(votes.getId()).get();
        // Disconnect from session so that the updates on updatedVotes are not directly saved in db
        em.detach(updatedVotes);
        VotesDTO votesDTO = votesMapper.toDto(updatedVotes);

        restVotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, votesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(votesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Votes in the database
        List<Votes> votesList = votesRepository.findAll();
        assertThat(votesList).hasSize(databaseSizeBeforeUpdate);
        Votes testVotes = votesList.get(votesList.size() - 1);

        // Validate the Votes in Elasticsearch
        verify(mockVotesSearchRepository).save(testVotes);
    }

    @Test
    @Transactional
    void putNonExistingVotes() throws Exception {
        int databaseSizeBeforeUpdate = votesRepository.findAll().size();
        votes.setId(count.incrementAndGet());

        // Create the Votes
        VotesDTO votesDTO = votesMapper.toDto(votes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, votesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(votesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Votes in the database
        List<Votes> votesList = votesRepository.findAll();
        assertThat(votesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Votes in Elasticsearch
        verify(mockVotesSearchRepository, times(0)).save(votes);
    }

    @Test
    @Transactional
    void putWithIdMismatchVotes() throws Exception {
        int databaseSizeBeforeUpdate = votesRepository.findAll().size();
        votes.setId(count.incrementAndGet());

        // Create the Votes
        VotesDTO votesDTO = votesMapper.toDto(votes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(votesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Votes in the database
        List<Votes> votesList = votesRepository.findAll();
        assertThat(votesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Votes in Elasticsearch
        verify(mockVotesSearchRepository, times(0)).save(votes);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVotes() throws Exception {
        int databaseSizeBeforeUpdate = votesRepository.findAll().size();
        votes.setId(count.incrementAndGet());

        // Create the Votes
        VotesDTO votesDTO = votesMapper.toDto(votes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVotesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(votesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Votes in the database
        List<Votes> votesList = votesRepository.findAll();
        assertThat(votesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Votes in Elasticsearch
        verify(mockVotesSearchRepository, times(0)).save(votes);
    }

    @Test
    @Transactional
    void partialUpdateVotesWithPatch() throws Exception {
        // Initialize the database
        votesRepository.saveAndFlush(votes);

        int databaseSizeBeforeUpdate = votesRepository.findAll().size();

        // Update the votes using partial update
        Votes partialUpdatedVotes = new Votes();
        partialUpdatedVotes.setId(votes.getId());

        restVotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVotes.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVotes))
            )
            .andExpect(status().isOk());

        // Validate the Votes in the database
        List<Votes> votesList = votesRepository.findAll();
        assertThat(votesList).hasSize(databaseSizeBeforeUpdate);
        Votes testVotes = votesList.get(votesList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateVotesWithPatch() throws Exception {
        // Initialize the database
        votesRepository.saveAndFlush(votes);

        int databaseSizeBeforeUpdate = votesRepository.findAll().size();

        // Update the votes using partial update
        Votes partialUpdatedVotes = new Votes();
        partialUpdatedVotes.setId(votes.getId());

        restVotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVotes.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVotes))
            )
            .andExpect(status().isOk());

        // Validate the Votes in the database
        List<Votes> votesList = votesRepository.findAll();
        assertThat(votesList).hasSize(databaseSizeBeforeUpdate);
        Votes testVotes = votesList.get(votesList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingVotes() throws Exception {
        int databaseSizeBeforeUpdate = votesRepository.findAll().size();
        votes.setId(count.incrementAndGet());

        // Create the Votes
        VotesDTO votesDTO = votesMapper.toDto(votes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, votesDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(votesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Votes in the database
        List<Votes> votesList = votesRepository.findAll();
        assertThat(votesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Votes in Elasticsearch
        verify(mockVotesSearchRepository, times(0)).save(votes);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVotes() throws Exception {
        int databaseSizeBeforeUpdate = votesRepository.findAll().size();
        votes.setId(count.incrementAndGet());

        // Create the Votes
        VotesDTO votesDTO = votesMapper.toDto(votes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(votesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Votes in the database
        List<Votes> votesList = votesRepository.findAll();
        assertThat(votesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Votes in Elasticsearch
        verify(mockVotesSearchRepository, times(0)).save(votes);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVotes() throws Exception {
        int databaseSizeBeforeUpdate = votesRepository.findAll().size();
        votes.setId(count.incrementAndGet());

        // Create the Votes
        VotesDTO votesDTO = votesMapper.toDto(votes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVotesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(votesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Votes in the database
        List<Votes> votesList = votesRepository.findAll();
        assertThat(votesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Votes in Elasticsearch
        verify(mockVotesSearchRepository, times(0)).save(votes);
    }

    @Test
    @Transactional
    void deleteVotes() throws Exception {
        // Initialize the database
        votesRepository.saveAndFlush(votes);

        int databaseSizeBeforeDelete = votesRepository.findAll().size();

        // Delete the votes
        restVotesMockMvc
            .perform(delete(ENTITY_API_URL_ID, votes.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Votes> votesList = votesRepository.findAll();
        assertThat(votesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Votes in Elasticsearch
        verify(mockVotesSearchRepository, times(1)).deleteById(votes.getId());
    }

    @Test
    @Transactional
    void searchVotes() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        votesRepository.saveAndFlush(votes);
        when(mockVotesSearchRepository.search("id:" + votes.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(votes), PageRequest.of(0, 1), 1));

        // Search the votes
        restVotesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + votes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(votes.getId().intValue())));
    }
}
