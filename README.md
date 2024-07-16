# Challenge

## How to run challenge code
Please run command
````
 docker-compose up
````
Solution has 3 docker image
- REST Java Backed on port 8080
- RabbitMQ 
- Postgres database
## Endpoints
There is 3 endpoint in program
- POST /temperature - endpoint to add new measurement
- GET /temperature/{city} - endpoint to get yearly average temperature for {city}
- POST /temperature/loadFromFile - endpoint to load examples form from file

## Solution
Data from csv file are parsed and sended to rabbitmq. In next step data are downloading from Queue and validated.
If measurement is correct then is added to statistic
In other case measurement is send to Rabbit error queue
