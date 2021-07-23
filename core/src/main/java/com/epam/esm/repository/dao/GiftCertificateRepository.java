package com.epam.esm.repository.dao;

import com.epam.esm.repository.model.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Integer> {

    List<GiftCertificate> findAllByNameContainsAndDescriptionContainsAndTagsNameIn(String name, String description,
                                                                                   Collection<String> tags_name,
                                                                                   Sort sort);

    List<GiftCertificate> findAllByNameContainsAndDescriptionContains(String name, String description, Sort sort);

    Page<GiftCertificate> findAllByNameContainsAndDescriptionContainsAndTagsNameIn(String name, String description,
                                                                                   Collection<String> tags_name,
                                                                                   Pageable pageable);

    Page<GiftCertificate> findAllByNameContainsAndDescriptionContains(String name, String description,
                                                                      Pageable pageable);
}
