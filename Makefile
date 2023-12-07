# Makefile.

compose:
	docker-compose build

up:
	docker-compose up

daemon:
	docker-compose up -d

down:
	docker-compose down

status:
	docker ps -a

clean:
	rm -rf ./target

.PHONY: compose up daemon down status clean