FROM postgres:18.4

ENV POSTGRES_DB=spring_data_jpa
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres

COPY database.sql /docker-entrypoint-initdb.d/database.sql

EXPOSE 5432