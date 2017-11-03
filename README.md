# Camel FTP-POC
> Simple POC to test some camel concepts

## Tricky parts

* Timezone configuration in order to check the timespan of the changed file
* FTP server image is really bad documented
* Producing and consuming from the same folder can result in unexpected behaviors
* readLocks values deserve attention
* pollingConsumerQueueSize can result in deadLocks

## Roadmap

* Write some tests

## Meta

Alex Rocha - [about.me](http://about.me/alex.rochas)
