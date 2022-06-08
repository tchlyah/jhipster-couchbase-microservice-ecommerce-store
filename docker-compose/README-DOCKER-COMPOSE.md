# JHipster generated Docker-Compose configuration

## Usage

Launch all your infrastructure by running: `docker-compose up -d`.

## Configured Docker services

### Service registry and configuration server:

- [JHipster Registry](http://localhost:8761)

### Applications and dependencies:

- store (gateway application)
- store's couchbase database
- product (microservice application)
- product's couchbase database
- invoice (microservice application)
- invoice's couchbase database
- notification (microservice application)
- notification's couchbase database

### Additional Services:

- [Prometheus server](http://localhost:9090)
- [Prometheus Alertmanager](http://localhost:9093)
- [Grafana](http://localhost:3000)
