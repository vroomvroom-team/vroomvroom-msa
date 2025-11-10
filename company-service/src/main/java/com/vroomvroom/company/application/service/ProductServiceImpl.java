package com.vroomvroom.company.application.service;

import com.vroomvroom.company.application.command.CreateProductCommand;
import com.vroomvroom.company.application.dto.ProductResult;
import com.vroomvroom.company.application.port.HubClient;
import com.vroomvroom.company.common.exception.CustomException;
import com.vroomvroom.company.common.exception.ErrorCode;
import com.vroomvroom.company.domain.entity.Company;
import com.vroomvroom.company.domain.entity.Product;
import com.vroomvroom.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final CompanyRepository companyRepository;

    private final HubClient hubClient;

    @Override
    @Transactional
    public ProductResult createProduct(CreateProductCommand command) {
        log.info("상품 등록 시작");

        Company company = companyRepository.findByCompanyIdAndDeletedAtIsNull(command.companyId())
                .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));

/*        TODO. 유저 권한 체크
        authorityValidator.validateCreateProductAuthority(
                command.hubId(),
                command.userId(),
                command.userRole()
        );*/

        validateHub(command.hubId());

        Product product = company.addProduct(
                command.hubId(),
                command.productName(),
                command.price()
        );

        companyRepository.save(company);

        log.info("상품 등록 완료: productId = {}", product.getProductId());
        return ProductResult.from(product);
    }

    public void validateHub(UUID hubId) {
        if (!hubClient.existsHub(hubId)) {
            throw new CustomException(ErrorCode.HUB_NOT_FOUND);
        }
    }
}
