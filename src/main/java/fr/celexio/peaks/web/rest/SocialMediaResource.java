package fr.celexio.peaks.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.celexio.peaks.domain.SocialMedia;
import fr.celexio.peaks.service.SocialMediaService;
import fr.celexio.peaks.web.rest.errors.BadRequestAlertException;
import fr.celexio.peaks.web.rest.util.HeaderUtil;
import fr.celexio.peaks.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SocialMedia.
 */
@RestController
@RequestMapping("/api")
public class SocialMediaResource {

    private final Logger log = LoggerFactory.getLogger(SocialMediaResource.class);

    private static final String ENTITY_NAME = "socialMedia";

    private final SocialMediaService socialMediaService;

    public SocialMediaResource(SocialMediaService socialMediaService) {
        this.socialMediaService = socialMediaService;
    }

    /**
     * POST  /social-medias : Create a new socialMedia.
     *
     * @param socialMedia the socialMedia to create
     * @return the ResponseEntity with status 201 (Created) and with body the new socialMedia, or with status 400 (Bad Request) if the socialMedia has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/social-medias")
    @Timed
    public ResponseEntity<SocialMedia> createSocialMedia(@RequestBody SocialMedia socialMedia) throws URISyntaxException {
        log.debug("REST request to save SocialMedia : {}", socialMedia);
        if (socialMedia.getId() != null) {
            throw new BadRequestAlertException("A new socialMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SocialMedia result = socialMediaService.save(socialMedia);
        return ResponseEntity.created(new URI("/api/social-medias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /social-medias : Updates an existing socialMedia.
     *
     * @param socialMedia the socialMedia to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated socialMedia,
     * or with status 400 (Bad Request) if the socialMedia is not valid,
     * or with status 500 (Internal Server Error) if the socialMedia couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/social-medias")
    @Timed
    public ResponseEntity<SocialMedia> updateSocialMedia(@RequestBody SocialMedia socialMedia) throws URISyntaxException {
        log.debug("REST request to update SocialMedia : {}", socialMedia);
        if (socialMedia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SocialMedia result = socialMediaService.save(socialMedia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, socialMedia.getId().toString()))
            .body(result);
    }

    /**
     * GET  /social-medias : get all the socialMedias.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of socialMedias in body
     */
    @GetMapping("/social-medias")
    @Timed
    public ResponseEntity<List<SocialMedia>> getAllSocialMedias(Pageable pageable) {
        log.debug("REST request to get a page of SocialMedias");
        Page<SocialMedia> page = socialMediaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/social-medias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /social-medias/:id : get the "id" socialMedia.
     *
     * @param id the id of the socialMedia to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the socialMedia, or with status 404 (Not Found)
     */
    @GetMapping("/social-medias/{id}")
    @Timed
    public ResponseEntity<SocialMedia> getSocialMedia(@PathVariable Long id) {
        log.debug("REST request to get SocialMedia : {}", id);
        Optional<SocialMedia> socialMedia = socialMediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(socialMedia);
    }

    /**
     * DELETE  /social-medias/:id : delete the "id" socialMedia.
     *
     * @param id the id of the socialMedia to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/social-medias/{id}")
    @Timed
    public ResponseEntity<Void> deleteSocialMedia(@PathVariable Long id) {
        log.debug("REST request to delete SocialMedia : {}", id);
        socialMediaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/social-medias?query=:query : search for the socialMedia corresponding
     * to the query.
     *
     * @param query the query of the socialMedia search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/social-medias")
    @Timed
    public ResponseEntity<List<SocialMedia>> searchSocialMedias(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SocialMedias for query {}", query);
        Page<SocialMedia> page = socialMediaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/social-medias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
