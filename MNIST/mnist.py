import numpy as np
import keras.datasets.mnist as mnist

(xtrain, ytrain), (xtest, ytest) = mnist.load_data()
xtrain = xtrain.reshape((60000,28*28))/255
xtest = xtest.reshape((10000,28*28))/255

zeros = np.zeros((60000,10))
for i in range(len(ytrain)):
    zeros[i][ytrain[i]] = 1
ytrain = zeros

zeros = np.zeros((10000,10))
for i in range(len(ytest)):
    zeros[i][ytest[i]] = 1
ytest = zeros

weights = np.random.rand(28*28,10)
weights = weights.reshape((28*28,10))
batch_size = 100
lr = 0.001
epochs = 100

def loss(x, y):
    return np.sum(np.power(x-y,2)/x.shape[0]/2)

def grad(y, x, w):
    return (1/len(y))*((x@w).T@x - y.T@x)

loss_history = []
validation_history = []
for epoch in range(epochs):
    print("epoch",epoch,"/",epochs)
    for i in range(int(len(xtrain)/batch_size)):
        x = xtrain[i*batch_size:(i+1)*batch_size]
        y = ytrain[i*batch_size:(i+1)*batch_size]
        output = x@weights
        loss_history.append(loss(output,y))
        weights -= lr * grad(y, x, weights).T
    validation_history.append(loss(xtest@weights,ytest))

import matplotlib.pyplot as plt

plt.plot(loss_history[100:])
plt.plot(np.linspace(0,len(loss_history)-100,len(validation_history)),validation_history)
plt.show()