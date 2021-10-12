package com.cev.accesoadatos.tema2.jhipster.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cev.accesoadatos.tema2.jhipster.domain.Categoria;
import com.cev.accesoadatos.tema2.jhipster.repository.CategoriaRepository;
import com.cev.accesoadatos.tema2.jhipster.repository.search.CategoriaSearchRepository;
import com.cev.accesoadatos.tema2.jhipster.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cev.accesoadatos.tema2.jhipster.domain.Categoria}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CategoriaResource {

    private static final String ENTITY_NAME = "categoria";
    private final Logger log = LoggerFactory.getLogger(CategoriaResource.class);
    private final CategoriaRepository categoriaRepository;
    private final CategoriaSearchRepository categoriaSearchRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public CategoriaResource(CategoriaRepository categoriaRepository, CategoriaSearchRepository categoriaSearchRepository) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaSearchRepository = categoriaSearchRepository;
    }

    /**
     * {@code POST  /categorias} : Create a new categoria.
     *
     * @param categoria the categoria to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoria, or with status {@code 400 (Bad Request)} if the categoria has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categorias")
    public ResponseEntity<Categoria> createCategoria(@Valid @RequestBody Categoria categoria) throws URISyntaxException {
        log.debug("REST request to save Categoria : {}", categoria);
        if (categoria.getId() != null) {
            throw new BadRequestAlertException("A new categoria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Categoria result = categoriaRepository.save(categoria);
        categoriaSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/categorias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categorias/:id} : Updates an existing categoria.
     *
     * @param id        the id of the categoria to save.
     * @param categoria the categoria to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoria,
     * or with status {@code 400 (Bad Request)} if the categoria is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoria couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categorias/{id}")
    public ResponseEntity<Categoria> updateCategoria(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Categoria categoria
    ) throws URISyntaxException {
        log.debug("REST request to update Categoria : {}, {}", id, categoria);
        if (categoria.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoria.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Categoria result = categoriaRepository.save(categoria);
        categoriaSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, categoria.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /categorias/:id} : Partial updates given fields of an existing categoria, field will ignore if it is null
     *
     * @param id        the id of the categoria to save.
     * @param categoria the categoria to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoria,
     * or with status {@code 400 (Bad Request)} if the categoria is not valid,
     * or with status {@code 404 (Not Found)} if the categoria is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoria couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categorias/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<Categoria> partialUpdateCategoria(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Categoria categoria
    ) throws URISyntaxException {
        log.debug("REST request to partial update Categoria partially : {}, {}", id, categoria);
        if (categoria.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoria.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Categoria> result = categoriaRepository
            .findById(categoria.getId())
            .map(existingCategoria -> {
                if (categoria.getNombre() != null) {
                    existingCategoria.setNombre(categoria.getNombre());
                }
                if (categoria.getImagen() != null) {
                    existingCategoria.setImagen(categoria.getImagen());
                }
                if (categoria.getImagenContentType() != null) {
                    existingCategoria.setImagenContentType(categoria.getImagenContentType());
                }

                return existingCategoria;
            })
            .map(categoriaRepository::save)
            .map(savedCategoria -> {
                categoriaSearchRepository.save(savedCategoria);

                return savedCategoria;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, categoria.getId().toString())
        );
    }

    /**
     * {@code GET  /categorias} : get all the categorias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categorias in body.
     */
    @GetMapping("/categorias")
    public List<Categoria> getAllCategorias() {
        log.debug("REST request to get all Categorias");
        return categoriaRepository.findAll();
    }

    /**
     * {@code GET  /categorias/:id} : get the "id" categoria.
     *
     * @param id the id of the categoria to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoria, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categorias/{id}")
    public ResponseEntity<Categoria> getCategoria(@PathVariable Long id) {
        log.debug("REST request to get Categoria : {}", id);
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(categoria);
    }

    /**
     * {@code DELETE  /categorias/:id} : delete the "id" categoria.
     *
     * @param id the id of the categoria to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        log.debug("REST request to delete Categoria : {}", id);
        categoriaRepository.deleteById(id);
        categoriaSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/categorias?query=:query} : search for the categoria corresponding
     * to the query.
     *
     * @param query the query of the categoria search.
     * @return the result of the search.
     */
    @GetMapping("/_search/categorias")
    public List<Categoria> searchCategorias(@RequestParam String query) {
        log.debug("REST request to search Categorias for query {}", query);
        return StreamSupport.stream(categoriaSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
