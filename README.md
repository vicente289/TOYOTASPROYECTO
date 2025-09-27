# Sistema de Admisión (Swing + MySQL) — UI Mejorada
Implementa HU1, HU2, HU3, HU8, HU16 con flujo guiado y bootstrap.

## Pasos
1) MySQL:
   ```sql
   SOURCE db/schema.sql;
   -- opcional:
   SOURCE db/seed.sql;
   ```
2) Configurar `src/main/resources/db.properties`.
3) Ejecutar:
   ```bash
   mvn clean package
   java -jar target/admision-swing-1.0.0-jar-with-dependencies.jar
   ```
