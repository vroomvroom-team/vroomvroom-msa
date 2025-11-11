package com.vroomvroom.company.application.service;

import com.vroomvroom.company.application.command.CreateProductCommand;
import com.vroomvroom.company.application.command.DeleteCommand;
import com.vroomvroom.company.application.command.UpdateProductCommand;
import com.vroomvroom.company.application.dto.ProductResult;
import com.vroomvroom.company.application.port.HubClient;
import com.vroomvroom.company.common.exception.CustomException;
import com.vroomvroom.company.common.exception.ErrorCode;
import com.vroomvroom.company.domain.entity.Company;
import com.vroomvroom.company.domain.entity.Product;
import com.vroomvroom.company.domain.repository.CompanyRepository;
import com.vroomvroom.company.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;

    private final HubClient hubClient;

    @Override
    @Transactional
    public ProductResult createProduct(CreateProductCommand command) {
        log.info("상품 등록 시작");

        Company company = getActiveCompany(command.companyId());
        validateHub(command.hubId());
        validateDuplicateName(command.productName());

        /*        TODO. 유저 권한 체크
        authorityValidator.validateCreateProductAuthority(
                command.hubId(),
                company.getCompanyManagerId(),
                command.userId(),
                command.userRole()
        );*/

        Product product = Product.create(
                company,
                command.hubId(),
                command.productName(),
                command.price()
        );

        productRepository.save(product);

        log.info("상품 등록 완료: productId = {}", product.getProductId());
        return ProductResult.from(product);
    }

    public void validateHub(UUID hubId) {
        if (!hubClient.existsHub(hubId)) {
            throw new CustomException(ErrorCode.HUB_NOT_FOUND);
        }
    }

    @Override
    public ProductResult getProduct(UUID productId) {
        log.info("상품 상세 조회 시작");

        Product product = getActiveProduct(productId);

        log.info("상품 상세 조회 완료: productId = {}", product.getProductId());
        return ProductResult.from(product);
    }

    @Override
    @Transactional
    public ProductResult updateProduct(UpdateProductCommand command) {
        Product product = getActiveProduct(command.productId());
        Company company = getActiveCompany(product.getCompany().getCompanyId());

/*        TODO. 유저 권한 체크
        authorityValidator.validateUpdateAuthority(
                product.getHubId(),
                company.getCompanyManagerId(),
                command.userId(),
                command.userRole()
        );*/

        companyUpdates(product, command);

        log.info("상품 수정 완료: productId = {}", product.getProductId());
        return ProductResult.from(product);
    }

    public void companyUpdates(Product product, UpdateProductCommand command) {
        if (command.productName() != null) {
            validateDuplicateName(command.productName());
            product.changeProductName(command.productName());
        }

        if (command.price() != null) {
            product.changePrice(command.price());
        }
    }

    @Override
    @Transactional
    public void deleteProduct(DeleteCommand command) {
        Product product = getActiveProduct(command.id());

        /*        TODO. 유저 권한 체크
        authorityValidator.validateDeleteAuthority(
                company.getHubId(),
                command.userId(),
                command.userRole()
        );*/

        product.markAsDeleted();
    }

    public void validateDuplicateName(String productName) {
        if (productRepository.existsByProductNameAndDeletedAtIsNull(productName)) {
            throw new CustomException(ErrorCode.DUPLICATE_PRODUCT_NAME);
        }
    }

    private Company getActiveCompany(UUID companyId) {
        return companyRepository.findByCompanyIdAndDeletedAtIsNull(companyId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));
    }

    private Product getActiveProduct(UUID productId) {
        return productRepository.findByProductIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
