package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.GiftCertificatesDao;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateService implements CommonService<GiftCertificateDto> {

    private final GiftCertificatesDao dao;

    @Autowired
    public GiftCertificateService(GiftCertificatesDao dao) {
        this.dao = dao;
    }

    @Override
    public boolean create(GiftCertificateDto entity) {
        return false;
    }

    @Override
    public Optional<GiftCertificateDto> read(int id) {
        return Optional.empty();
    }

    @Override
    public List<GiftCertificateDto> readAll() {
        return null;
    }

    @Override
    public Optional<GiftCertificateDto> update(GiftCertificateDto entity) {
        return Optional.empty();
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
