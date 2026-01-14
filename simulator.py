import pika
import datetime

connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()

channel.basic_publish(exchange='sync-exchange',
                      routing_key='sync.monitor.generated',
                      body='Hello World!')

print("Message sent!")

connection.close()

startTime = datetime.datetime.now()
print(startTime)
print(startTime.hour)
