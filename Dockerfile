FROM openjdk:11-jre-slim

ENV DIR=/home/egdbag/bot

RUN mkdir -p $DIR
COPY com.egdbag.covid.bot/target/covid-bot-0.1.0-SNAPSHOT-jar-with-dependencies.jar $DIR/covid-bot-0.1.0-SNAPSHOT-jar-with-dependencies.jar

CMD java -jar /home/egdbag/bot/covid-bot-0.1.0-SNAPSHOT-jar-with-dependencies.jar