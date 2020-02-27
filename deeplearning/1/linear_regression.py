import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D


def linear(x, w):
    return x@w

def loss(x, y):
    return np.sum(np.power(x-y,2)/x.shape[0]/2)


def grad(y, x, w):
    return (1/len(y))*(x@w@x - y@x)


size = 10000
ground_truth = np.array([0.2,0.5,-0.25, 1])
arr1 = np.random.normal(0,1,size)
arr2 = np.random.uniform(0,1,size)
arr3 = np.random.normal(1,2,size)
arr4 = np.ones_like(arr1)
data = np.stack([arr1,arr2,arr3, arr4], axis=1)

y = linear(data, ground_truth) + np.random.normal(0,0.1,size)

weights = np.array([1,1,1,1])
output = linear(data, weights)

fig = plt.figure()
ax = Axes3D(fig)
ax.scatter(data[:,0],data[:,1],data[:,2], s=1, c = y)
plt.show()

# fig = plt.figure()
# ax = Axes3D(fig)
# ax.scatter(data[:,0],data[:,1],data[:,2], s=1, c = output)
# plt.show()

fig = plt.figure()
ax = Axes3D(fig)
ax.scatter(data[:,0],data[:,1],data[:,2], s=1, c = y-output)
plt.show()

weights = np.array([1.,1.,1.,1.])
lr = 0.01

for i in range(10000):
    output = linear(data, weights)
    print(loss(output, y))
    weights -= lr * grad(y, data, weights)





def gradient(w, x, y):
    return (1/len(y))*(x@w@x - y@x)

def gradient_descent_step(b, X, y, lr):
    return b - lr * gradient(b, data, y)

def loss(x, y):
    return np.sum(np.power(x-y,2)/x.shape[0]/2)



