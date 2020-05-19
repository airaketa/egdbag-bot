# Запуск
```
mvn clean install -f com.egdbag.covid.bot/pom.xml
docker build -t egdbag/bot:0.1.0 .
docker run --network host -v `pwd`/config/config.yaml:/home/egdbag/bot/config.yaml -e CONFIG_FILE=/home/egdbag/bot/config.yaml egdbag/bot:0.1.0
```

# Docker Hub
[Образ от 19/05/20](https://hub.docker.com/repository/docker/airaketa/covid-bot/general)
