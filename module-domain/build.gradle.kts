dependencies {
    implementation(project(":module-infrastructure:persistence-database"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.security:spring-security-crypto:6.2.1")
}