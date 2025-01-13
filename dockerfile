FROM openjdk:17

EXPOSE 9000

COPY ./target/multiCurrencyWalletApp-0.0.1-SNAPSHOT.jar multiCurrencyWalletApp.jar

ENTRYPOINT ["java","-jar","/multiCurrencyWalletApp.jar"]