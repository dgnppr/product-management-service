rootProject.name = "product-management-service"
include("module-infrastructure")
include("module-domain")
include("module-interface")
include("module-infrastructure:persistence-database")
findProject(":module-infrastructure:persistence-database")?.name = "persistence-database"
include("module-infrastructure:persistence-redis-adapter")
findProject(":module-infrastructure:persistence-redis-adapter")?.name = "persistence-redis-adapter"
include("module-common")
