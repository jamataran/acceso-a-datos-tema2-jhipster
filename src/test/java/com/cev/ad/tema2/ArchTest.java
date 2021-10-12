package com.cev.ad.tema2;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.cev.ad.tema2");

        noClasses()
            .that()
            .resideInAnyPackage("com.cev.ad.tema2.service..")
            .or()
            .resideInAnyPackage("com.cev.ad.tema2.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.cev.ad.tema2.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
