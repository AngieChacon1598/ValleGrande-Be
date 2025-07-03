# AS232S4_T02-be

---

## ✅ 1. Configura `application.yml` con tus credenciales

Configura el `application.yml` donde se especifican `username: ${DB_USERNAME}` y `password: ${DB_PASSWORD}`:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@SistemaGestionVentas_medium?TNS_ADMIN=Wallet_SistemaGestionVentas
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: oracle.jdbc.OracleDriver
````

Asegúrate de definir las variables de entorno `DB_USERNAME` y `DB_PASSWORD`.

---

## ✅ 2. Instalar Java 17

Ejecuta los siguientes comandos en tu terminal:

```bash
chmod +x mvnw
sudo apt update
sudo apt install openjdk-17-jdk -y
```

---

## ✅ 3. Verificar la instalación

Después de instalar, asegúrate de que Java 17 esté disponible ejecutando:

```bash
update-alternatives --config java
```

Verás una lista como esta:

```
There are 2 choices for the alternative java (providing /usr/bin/java).

  Selection    Path                                            Priority   Status
------------------------------------------------------------
* 0            /usr/lib/jvm/java-11-openjdk-amd64/bin/java     1111      auto mode
  1            /usr/lib/jvm/java-11-openjdk-amd64/bin/java     1111      manual mode
  2            /usr/lib/jvm/java-17-openjdk-amd64/bin/java     1112      manual mode
```

Selecciona la opción que corresponde a Java 17 (probablemente la opción 2) y presiona `Enter`.

---

## ✅ 4. Configurar `JAVA_HOME`

Establece la variable `JAVA_HOME` apuntando a Java 17:

```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

Para aplicar esto automáticamente cada vez que abras el terminal, agrega esas dos líneas al final del archivo `~/.bashrc` o `~/.zshrc`, dependiendo del shell que estés usando:

```bash
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

---

## ✅ 5. Verifica que todo esté bien

Ahora ejecuta:

```bash
java -version
echo $JAVA_HOME
```

Debes ver algo similar a:

```
openjdk version "17..."
/usr/lib/jvm/java-17-openjdk-amd64
```

---

## ✅ 6. Corre la aplicación en Codespace

Ahora que tienes Java 17 listo, puedes compilar y correr tu app:

```bash
mvn clean install
mvn spring-boot:run
```

---

✔ ¡Listo! La aplicación ya está lista para usarse.
