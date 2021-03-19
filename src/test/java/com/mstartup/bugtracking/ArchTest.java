package com.mstartup.bugtracking;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.mstartup.bugtracking");

        noClasses()
            .that()
                .resideInAnyPackage("com.mstartup.bugtracking.service..")
            .or()
                .resideInAnyPackage("com.mstartup.bugtracking.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.mstartup.bugtracking.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
