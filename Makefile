#!/usr/bin/make -f
PROJECT_NAME := 'Hospital AccWe'
DOCKER_COMPOSE_FILE := $(shell pwd)/docker-compose-dev.yml
WEB_SERVICE := web
DOCKER_PROJECT_NAME := HA


dev-up:
	docker-compose -p $(DOCKER_PROJECT_NAME) -f $(DOCKER_COMPOSE_FILE)  up --build

bash:
	docker-compose -p $(DOCKER_PROJECT_NAME) -f $(DOCKER_COMPOSE_FILE) exec  $(WEB_SERVICE) bash
