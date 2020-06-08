ICQ New bot for tracking COVID-19 cases in Moscow.

Winner of the online [VirusHack](https://online.innoagency.ru/virushack/?roistat_visit=151179&_ga=2.133161742.182489988.1591626101-2087765081.1588001513) hackathon.

https://habr.com/ru/post/502104/

# Сheck it out
https://icq.im/localbot

# Run
```
docker run --network host -v `pwd`/config/config.yaml:/home/egdbag/bot/config.yaml -e CONFIG_FILE=/home/egdbag/bot/config.yaml egdbag/bot:0.1.0
```
# Build from source
```
mvn clean install -f com.egdbag.covid.bot/pom.xml
docker build -t egdbag/bot:0.1.0 .
```
# Docker Hub
https://hub.docker.com/repository/docker/airaketa/covid-bot
