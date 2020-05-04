FROM openjdk:11-jre-slim

ENV DIR=/home/egdbag/bot

RUN mkdir -p $DIR
ADD com.egdbag.covid.bot/target/covid-bot-0.1.0-SNAPSHOT-jar-with-dependencies $DIR

CMD /home/egdbag/bot/covid-bot-0.1.0-SNAPSHOT-jar-with-dependencies