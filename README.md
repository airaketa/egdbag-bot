# Запуск
```
mvn clean install -f com.egdbag.covid.bot/pom.xml
docker build -t egdbag/bot:0.1.0 .
docker run --network host -v `pwd`/config/config.yaml:/home/egdbag/bot/config.yaml -e CONFIG_FILE=/home/egdbag/bot/config.yaml egdbag/bot:0.1.0
```
