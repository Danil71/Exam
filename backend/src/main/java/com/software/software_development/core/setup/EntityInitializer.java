package com.software.software_development.core.setup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.log.Loggable;
import com.software.software_development.model.entity.CategoryEntity;
import com.software.software_development.model.entity.ProductEntity;
import com.software.software_development.model.entity.ReviewEntity;
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.model.enums.UserRole;
import com.software.software_development.service.entity.CategoryService;
import com.software.software_development.service.entity.ProductService;
import com.software.software_development.service.entity.PurchaseService;
import com.software.software_development.service.entity.ReviewService;
import com.software.software_development.service.entity.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntityInitializer {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final PurchaseService purchaseService;
    private final ReviewService reviewService;
    private final UserService userService;

    @Loggable
    @Transactional
    public void initializeAll() {
        if (!userService.getAll().isEmpty()) {
            refreshSearchFields();
            return;
        }
        List<UserEntity> users = createUsers();
        List<CategoryEntity> categories = createCategories();
        List<ProductEntity> products = createProducts(users, categories);
        createPurchases(users, products);
        createReviews(users, products);
        refreshSearchFields();
    }

    @Transactional
    public void refreshSearchFields() {
        reviewService.ensurePurchasesForExistingReviews();
        productService.refreshSearchFields();
        reviewService.refreshSearchFields();
    }

    private List<UserEntity> createUsers() {
        List<UserEntity> users = new ArrayList<>();
        users.add(userService.create(new UserEntity(
                "admin",
                "admin@shop.ru",
                "Admin123",
                UserRole.MANAGER
        )));
        users.add(userService.create(new UserEntity(
                "ivanov",
                "ivanov@shop.ru",
                "User1234",
                UserRole.USER
        )));
        users.add(userService.create(new UserEntity(
                "petrova",
                "petrova@shop.ru",
                "User1234",
                UserRole.USER
        )));
        return users;
    }

    private List<CategoryEntity> createCategories() {
        List<CategoryEntity> categories = new ArrayList<>();
        categories.add(categoryService.create(new CategoryEntity("Электроника", "Гаджеты и цифровая техника")));
        categories.add(categoryService.create(new CategoryEntity("Дом", "Товары для дома и кухни")));
        categories.add(categoryService.create(new CategoryEntity("Спорт", "Товары для спорта и отдыха")));
        categories.add(categoryService.create(new CategoryEntity("Книги", "Печатные и учебные издания")));
        return categories;
    }

    private List<ProductEntity> createProducts(List<UserEntity> users, List<CategoryEntity> categories) {
        List<ProductEntity> products = new ArrayList<>();
        products.add(productService.create(
                new ProductEntity("Смартфон Nova X", "Смартфон с OLED-экраном и 128 ГБ памяти", new BigDecimal("34990.00"), 12, users.get(0)),
                users.get(0).getId(),
                List.of(categories.get(0).getId())));
        products.add(productService.create(
                new ProductEntity("Наушники AirBeat", "Беспроводные наушники с шумоподавлением", new BigDecimal("8990.00"), 25, users.get(0)),
                users.get(0).getId(),
                List.of(categories.get(0).getId(), categories.get(2).getId())));
        products.add(productService.create(
                new ProductEntity("Кофеварка Morning", "Капельная кофеварка на 1,5 литра", new BigDecimal("4990.00"), 8, users.get(1)),
                users.get(1).getId(),
                List.of(categories.get(1).getId())));
        products.add(productService.create(
                new ProductEntity("Рюкзак City 25", "Городской рюкзак с отделением для ноутбука", new BigDecimal("3290.00"), 18, users.get(1)),
                users.get(1).getId(),
                List.of(categories.get(2).getId())));
        products.add(productService.create(
                new ProductEntity("Java для начинающих", "Практическое пособие по Java и Spring", new BigDecimal("1590.00"), 30, users.get(2)),
                users.get(2).getId(),
                List.of(categories.get(3).getId())));
        products.add(productService.create(
                new ProductEntity("Умная лампа Glow", "LED-лампа с регулировкой яркости и цвета", new BigDecimal("2190.00"), 14, users.get(2)),
                users.get(2).getId(),
                List.of(categories.get(0).getId(), categories.get(1).getId())));
        return products;
    }

    private void createReviews(List<UserEntity> users, List<ProductEntity> products) {
        reviewService.create(new ReviewEntity(null, null, "Отличный экран", "Яркий дисплей и хорошая автономность."),
                products.get(0).getId(), users.get(1).getId(), 5);
        reviewService.create(new ReviewEntity(null, null, "Хороший звук", "Наушники удобные, шумоподавление работает стабильно."),
                products.get(1).getId(), users.get(2).getId(), 4);
        reviewService.create(new ReviewEntity(null, null, "Для кухни подходит", "Кофе готовит быстро, корпус легко мыть."),
                products.get(2).getId(), users.get(0).getId(), 4);
        reviewService.create(new ReviewEntity(null, null, "Удобный рюкзак", "Много карманов и плотная спинка."),
                products.get(3).getId(), users.get(2).getId(), 5);
        reviewService.create(new ReviewEntity(null, null, "Полезная книга", "Много примеров, подойдет для учебного проекта."),
                products.get(4).getId(), users.get(1).getId(), 5);
        reviewService.create(new ReviewEntity(null, null, "Нормальная лампа", "Настройка цвета удобная, приложение простое."),
                products.get(5).getId(), users.get(0).getId(), 3);
    }

    private void createPurchases(List<UserEntity> users, List<ProductEntity> products) {
        purchaseService.buy(products.get(0).getId(), users.get(1).getId());
        purchaseService.buy(products.get(1).getId(), users.get(2).getId());
        purchaseService.buy(products.get(2).getId(), users.get(0).getId());
        purchaseService.buy(products.get(3).getId(), users.get(2).getId());
        purchaseService.buy(products.get(4).getId(), users.get(1).getId());
        purchaseService.buy(products.get(5).getId(), users.get(0).getId());
    }
}
