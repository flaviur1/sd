import pika
import json
import random
import sys
import time
from datetime import datetime

if len(sys.argv) != 2:
    print("the only argument should be the device id")
    sys.exit(1)

device_id = sys.argv[1]

try:
    connection_params = pika.ConnectionParameters(
        host='localhost',
        port=5672,
    )
    rabbit_connection = pika.BlockingConnection(connection_params)
    message_channel = rabbit_connection.channel()
    print("connection opened")
    
except Exception as error:
    print("failed to opened the connection")
    sys.exit(1)

start_timestamp = int(time.time())
reading_counter = 0

try:
    while True:
        simulated_timestamp = start_timestamp + (reading_counter * 600)
        formatted_time = datetime.fromtimestamp(simulated_timestamp).strftime("%Y-%m-%dT%H:%M:%S")
        energy_consumption = round(random.uniform(10.0, 150.0), 2)
        
        reading_data = {
            "deviceId": device_id,
            "value": energy_consumption,
            "timestamp": formatted_time
        }
        json_payload = json.dumps(reading_data)
        message_channel.basic_publish(
            exchange='sync-exchange',
            routing_key='sync.monitor.generated',
            body=json_payload
        )
        
        reading_counter += 1
        print(f"reading #{reading_counter}: time={formatted_time}, value={energy_consumption}")
        
        time.sleep(10)
    
finally:
    if rabbit_connection and rabbit_connection.is_open:
        rabbit_connection.close()
        print("connection closed")
