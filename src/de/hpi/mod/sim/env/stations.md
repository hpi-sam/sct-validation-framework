# Station-Reservation-Protocol

## Entities

- **Station:** Contains (3) Batteries and (5) Queue-Positions. Is used to get new packages or recharge. A Drive-Lock checks that only 1 Robot drives from arrival to queue or battery to queue at a time
- **Queue:** Contains all robots how want to drive to the station. If all queue-positions are reserved, no robot can drive to the station
- **Battery:** While inside a battery, a robot can recharge. A robot has to wait for permission to leave a battery and drive to the queue
- **Station-Range:** The Stations which are in use. Can be increased if needed.

## Goals

- Only 1 robot should drive from a stations arrival-position or battery to the queue-start at the same time (Drive-Lock)
- If a stations queue is full it will not get a reservation for a new robot until a queue-position is free
- A robots target position in the station is either a battery or loading-position
- Each station from the default station range should be accessed at the same rate
- The station range is only increased if no other stations have free batteries or queue-positions

## Protocol

### Without Charging

1. A robot has unloaded a package and wants to enter a station, to get a new one. It asks the dispatcher for a reservation on the queue of a station. If there is no station with a free queue position a new station is added to the range. 
2. The robot drives to the arrival-position of the station. There it requests entering the station which is granted if no other robot is currently driving in the station to the queue or battery
3. The robot drives to the loading-position
4. When it arrives in the queue it notifies the station so the drive lock can be opened
5. When leaving the station the robot notifies the station which frees a queue-position

### With Charging

1. A robot has unloaded a package and wants to charge. It asks the dispatcher for a battery reservation on a station. The station does not need to have a free queue-position. If there is no station with a free battery a new station is added to the range
2. The robot drives to the arrival-position of the station. There it requests entering the station which is granted if no other robot is currently driving in the station to the queue or battery
3. The robot drives to a battery
4. After fully charging it requests to leave the battery which is granted if no other robot is currently driving in the station to the queue or battery and there is a free queue-position
5. When it arrives in the queue it notifies the station so the drive lock can be opened
6. When leaving the station the robot notifies the station which frees a queue-position