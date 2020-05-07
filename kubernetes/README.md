# Running Spring Stocks on Kubernetes

## Prerequisites
* kubernetes environment up and running
* App images built and deployed to dockerhub

## Setting up RabbitMQ
RabbitMQ is setup with a helm chart. Use the following commmands to install RabbitMQ in the `default` namespace:

```bash
$ helm repo add bitnami https://charts.bitnami.com/bitnami
$ helm install my-release bitnami/rabbitmq
```

See `rabbitmq.txt` for getting access to the user and password for rabbitmq.

## Setting up the `spring-stocks` namespace
Setup a namespace for the `spring-stocks` portfolio of applications:

```bash
$ kubectl -n spring-stocks apply -f spring-stocks-namespace.yml
```

The default `serviceaccount` needs to be able to read from `configmaps` in the namespace, grant access by issuing:

```bash
$ kubectl -n spring-stocks apply -f spring-stocks-serviceaccount.yml
```

In order for the apps to be able to access RabbitMQ, a `secret` needs to be established to store common details like
the username, password, host and port. The file `spring-stocks-rabbitmq-secret` contains all the values, however, the 
password will need to be updated for each installation.

Get the RabbitMQ password:
```bash
$ $(kubectl get secret --namespace default my-release-rabbitmq -o jsonpath="{.data.rabbitmq-erlang-cookie}" | base64 --decode)
```

Now encode this value:
```bash
$ echo '[RESULT FROM LAST COMMAND]' | base64
```

Place this value in the `password` data item in `spring-stocks-rabbitmq-secret`.

Create the `secret`:
```bash
$ kubectl -n spring-stocks apply -f spring-stocks-rabbitmq-secret.yml
```

## Deploy the Stock Service
Every app will have a `configmap` and a `deployment` that needs to be created.

Create the `configmap`:
```bash
$ kubectl -n spring-stocks apply -f stock-service-configmap.yml
```

Create the `deployment`:
```bash
$ kubectl -n spring-stocks apply -f stock-service-deployment.yml
```

### Proxy access to the Stock Service
In order to access the Stock Service from the browser we need to proxy the call:
```bash
$ kubectl port-forward --namespace spring-stocks deploy/stock-service-deployment 9080:8080
```

Now access the service at http://localhost:9080/actuator/health

_NOTE:_ Ingress should be setup for this service to eliminate the need for explict proxying.

### Deploy a new image of Stock Service

```bash
$ k -n spring-stocks rollout restart deploy stock-service-deployment
```

### Deleting the Stock Service
If you should need to delete the Stock Service, issue:
```bash
$ kubectl -n spring-stocks delete deploy stock-service-deployment
```

## Deploy the Market Service
Every app will have a `configmap` and a `deployment` that needs to be created.

Create the `configmap`:
```bash
$ kubectl -n spring-stocks apply -f market-service-configmap.yml
```

Create the `deployment`:
```bash
$ kubectl -n spring-stocks apply -f market-service-deployment.yml
```

Expose the API Service to other Pods
```bash
$ kubectl -n spring-stocks apply -f market-service-service.yml
```

### Proxy access to the Market Service
In order to access the Market Service from the browser we need to proxy the call:
```bash
$ kubectl port-forward --namespace spring-stocks deploy/market-service-deployment 9081:8080
```
Now access the service at http://localhost:9081/actuator/health

_NOTE:_ Ingress should be setup for this service to eliminate the need for explict proxying.

### Deploy a new image of Market Service

```bash
$ k -n spring-stocks rollout restart deploy market-service-deployment
```

### Deleting the Market Service
If you should need to delete the Market Service, issue:
```bash
$ kubectl -n spring-stocks delete deploy market-service-deployment
```
